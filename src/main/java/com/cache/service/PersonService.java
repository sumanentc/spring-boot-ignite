package com.cache.service;

import com.cache.entity.Person;
import com.cache.exception.ResourceNotFoundException;
import com.cache.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.lang.IgniteFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private Ignite igniteInstance;

    public Person addPerson(Person person){
        long id = new Random().nextLong();
        //We cannot use the Spring Crud repository
        return personRepository.save(id,person);
    }

    public Person updatePerson(Person person,long personId){
        return personRepository.findById(personId)
                .map(existingPerson ->{
                    existingPerson.setLastName(person.getLastName());
                    existingPerson.setFirstName(person.getFirstName());
                    existingPerson.setAge(person.getAge());
                    return personRepository.save(personId,existingPerson);
                }).orElseGet(()->{
                    long id = new Random().nextLong();
                    return personRepository.save(id,person);
                });
    }

    public Person getPerson(long id){
        return personRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Person not found with id "+id));
    }

    public void deletePerson(Long id){
        personRepository.deleteById(id);
    }

    public List<Person> getPersons(String firstName, Integer age){
        if(firstName != null) {
            return personRepository.findByFirstNameLike(firstName);
        }else if(age != null){
            return personRepository.selectPersonsByAge(age);
        }else{
            List<Person> target = new ArrayList<>();
            personRepository.findAll().forEach(target::add);
            return target;
        }
    }


    /**
     * Fills the repository in with some sample data.
     */
    @PostConstruct
    private  void populateRepository() {
        Map<Long, Person> persons = new TreeMap<>();

        persons.put(1L, new Person(1L, "John", "Smith", 30));
        persons.put(2L, new Person(2L, "Brad", "Pitt", 25));
        persons.put(3L, new Person(3L,  "Mark", "Tomson", 32));
        persons.put(4L, new Person(4L,  "Erick", "Smith", 22));
        persons.put(5L, new Person(5L,"John", "Rozenberg", 35));
        persons.put(6L, new Person(6L,"Denis", "Won", 22));
        persons.put(7L, new Person(7L,"Abdula", "Adis",40));
        persons.put(8L, new Person(8L,  "Roman", "Ive", 27));
        /**
         * Using IgniteDataStreamer to populate the Cache. IgniteDataStreamer should be used for bulk operation
         */
        // Create a streamer to stream words into the cache.
        //https://www.javadoc.io/doc/org.apache.ignite/ignite-core/2.5.0/org/apache/ignite/IgniteDataStreamer.html
        try (IgniteDataStreamer<Long, Person> stmr = igniteInstance.dataStreamer("PersonCache")) {
            // Allow data updates.
            stmr.allowOverwrite(true);
            /*
            // Configure data transformation to count random numbers added to the stream.
            stmr.receiver(StreamTransformer.from((e, arg) -> {
                // Get current count.
                Person person = e.getValue();
                log.info("Existing Value ::: " + person);

                log.info("Key Present in Cache " + e.getKey());

                return null;
            }));
            */
            IgniteFuture<?> future =  stmr.addData(persons);
            stmr.flush();
            future.get();

        }
        log.info("\n>>> Added " + personRepository.count() + " Persons into the repository.");
    }
}

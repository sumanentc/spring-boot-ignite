package com.cache.repository;

import com.cache.entity.Person;
import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.Query;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;

import java.util.List;

@RepositoryConfig(cacheName = "PersonCache")
public interface PersonRepository extends IgniteRepository<Person, Long> {

    /**
     * Gets all the persons with the given name.
     * @param name Person name.
     * @return A list of Persons with the given first name.
     */
    List<Person> findByFirstNameLike(String name);

    /**
     * Getting ids of all the Person satisfying the custom query from {@link Query} annotation.
     *
     * @param age Query parameter.
     * @return A list of Persons' .
     */
    @Query("SELECT p.* FROM Person p WHERE age > ?")
    List<Person> selectPersonsByAge(int age);

    // SQL clause query which joins on 2 types to select people for a specific organization.
    @Query(" select pers.* from Person as pers, ORG_CACHE.Organization as org where pers.orgId = org.id and lower(org.name) = lower(?)")
    List<?> selectPersonByOrganization(String organizationName);


}

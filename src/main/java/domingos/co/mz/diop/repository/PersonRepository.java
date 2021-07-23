package domingos.co.mz.diop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import domingos.co.mz.diop.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}

package domingos.co.mz.diop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domingos.co.mz.diop.dto.request.PersonDTO;
import domingos.co.mz.diop.dto.response.MessageResponseDTO;
import domingos.co.mz.diop.entity.Person;
import domingos.co.mz.diop.exception.PersonNotFoundException;
import domingos.co.mz.diop.mapper.PersonMapper;
import domingos.co.mz.diop.repository.PersonRepository;
import domingos.co.mz.diop.utils.PersonUtils;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	 @Mock
	    private PersonRepository personRepository;

	    @Mock
	    private PersonMapper personMapper;

	    @InjectMocks
	    private PersonService personService;

	    @Test
	    void testGivenPersonDTOThenReturnSuccessSavedMessage() {
	        PersonDTO personDTO = PersonUtils.createFakeDTO();
	        Person expectedSavedPerson = PersonUtils.createFakeEntity();

	        when(personMapper.toModel(personDTO)).thenReturn(expectedSavedPerson);
	        when(personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);

	        MessageResponseDTO successMessage = personService.createPerson(personDTO);

	        assertEquals("Person successfully created with ID 1", successMessage.getMessage());
	    }

	    @Test
	    void testGivenValidPersonIdThenReturnThisPerson() throws PersonNotFoundException {
	        PersonDTO expectedPersonDTO = PersonUtils.createFakeDTO();
	        Person expectedSavedPerson =PersonUtils.createFakeEntity();
	        expectedPersonDTO.setId(expectedSavedPerson.getId());

	        when(personRepository.findById(expectedSavedPerson.getId())).thenReturn(Optional.of(expectedSavedPerson));
	        when(personMapper.toDTO(expectedSavedPerson)).thenReturn(expectedPersonDTO);

	        PersonDTO personDTO = personService.findById(expectedSavedPerson.getId());

	        assertEquals(expectedPersonDTO, personDTO);

	        assertEquals(expectedSavedPerson.getId(), personDTO.getId());
	        assertEquals(expectedSavedPerson.getFirstName(), personDTO.getFirstName());
	    }

	    @Test
	    void testGivenInvalidPersonIdThenThrowException() {
	        var invalidPersonId = 1L;
	        when(personRepository.findById(invalidPersonId))
	                .thenReturn(Optional.ofNullable(any(Person.class)));

	        assertThrows(PersonNotFoundException.class, () -> personService.findById(invalidPersonId));
	    }

	    @Test
	    void testGivenNoDataThenReturnAllPeopleRegistered() {
	        List<Person> expectedRegisteredPeople = Collections.singletonList(PersonUtils.createFakeEntity());
	        PersonDTO personDTO = PersonUtils.createFakeDTO();

	        when(personRepository.findAll()).thenReturn(expectedRegisteredPeople);
	        when(personMapper.toDTO(any(Person.class))).thenReturn(personDTO);

	        List<PersonDTO> expectedPeopleDTOList = personService.listAll();

	        assertFalse(expectedPeopleDTOList.isEmpty());
	        assertEquals(expectedPeopleDTOList.get(0).getId(), personDTO.getId());
	    }

	    @Test
	    void testGivenValidPersonIdAndUpdateInfoThenReturnSuccesOnUpdate() throws PersonNotFoundException {
	        var updatedPersonId = 2L;

	        PersonDTO updatePersonDTORequest = PersonUtils.createFakeDTO();
	        updatePersonDTORequest.setId(updatedPersonId);
	        updatePersonDTORequest.setLastName("Peleias updated");

	        Person expectedPersonToUpdate = PersonUtils.createFakeEntity();
	        expectedPersonToUpdate.setId(updatedPersonId);

	        Person expectedPersonUpdated = PersonUtils.createFakeEntity();
	        expectedPersonUpdated.setId(updatedPersonId);
	        expectedPersonToUpdate.setLastName(updatePersonDTORequest.getLastName());

	        when(personRepository.findById(updatedPersonId)).thenReturn(Optional.of(expectedPersonUpdated));
	        when(personMapper.toModel(updatePersonDTORequest)).thenReturn(expectedPersonUpdated);
	        when(personRepository.save(any(Person.class))).thenReturn(expectedPersonUpdated);

	        MessageResponseDTO successMessage = personService.updateById(updatedPersonId, updatePersonDTORequest);

	        assertEquals("Person successfully updated with ID 2", successMessage.getMessage());
	    }

	    @Test
	    void testGivenInvalidPersonIdAndUpdateInfoThenThrowExceptionOnUpdate() throws PersonNotFoundException {
	        var invalidPersonId = 1L;

	        PersonDTO updatePersonDTORequest = PersonUtils.createFakeDTO();
	        updatePersonDTORequest.setId(invalidPersonId);
	        updatePersonDTORequest.setLastName("Peleias updated");

	        when(personRepository.findById(invalidPersonId))
	                .thenReturn(Optional.ofNullable(any(Person.class)));

	        assertThrows(PersonNotFoundException.class, () -> personService.updateById(invalidPersonId, updatePersonDTORequest));
	    }

	    @Test
	    void testGivenValidPersonIdThenReturnSuccesOnDelete() throws PersonNotFoundException {
	        var deletedPersonId = 1L;
	        Person expectedPersonToDelete = PersonUtils.createFakeEntity();

	        when(personRepository.findById(deletedPersonId)).thenReturn(Optional.of(expectedPersonToDelete));
	        personService.delete(deletedPersonId);

	        verify(personRepository, times(1)).deleteById(deletedPersonId);
	    }

	    @Test
	    void testGivenInvalidPersonIdThenReturnSuccesOnDelete() throws PersonNotFoundException {
	        var invalidPersonId = 1L;
	        
	        when(personRepository.findById(invalidPersonId))
	                .thenReturn(Optional.ofNullable(any(Person.class)));

	        assertThrows(PersonNotFoundException.class, () -> personService.delete(invalidPersonId));
	    }

	}
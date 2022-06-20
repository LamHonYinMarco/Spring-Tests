package com.example.demo;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class RestControllerTest {

    @Autowired
    private TestObjectRepository testObjectRepository;

    // Get all
    @GetMapping("/data")
    public List<TestObject> getAllTestObjects() {
        return testObjectRepository.findAll();
    }

    // Get by id
    @GetMapping("/data/{id}")
    public TestObject getTestObject(@PathVariable Integer id) {
        Optional<TestObject> testObject = testObjectRepository.findById(id);

        if (!testObject.isPresent())
            throw new DataNotFoundException("id-" + id);

        return testObject.get();
    }

    // Delete by id
    @DeleteMapping("/data/{id}")
    public void deleteData(@PathVariable Integer id) {
        testObjectRepository.deleteById(id);
    }

    // Create data
    @PostMapping("/data")
    public ResponseEntity<Object> createData(@RequestBody TestObject testObject) {
        TestObject saveTestObject = testObjectRepository.save(testObject);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saveTestObject.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    // Edit data
    @PutMapping("/data/{id}")
    public ResponseEntity<Object> updateData(@RequestBody TestObject testObject, @PathVariable Integer id) {
        Optional<TestObject> optionalTestObject = testObjectRepository.findById(id);
        if (!optionalTestObject.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        testObject.setId(id);

        testObjectRepository.save(testObject);

        return ResponseEntity.noContent().build();

    }
}

package org.inigma.shared.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/datastore.xml", "/search.xml" })
public class SearchServiceITest {
    @Autowired
    private MongoOperations mongo;
    @Autowired
    private SearchService service;

    @Before
    public void setup() {
        mongo.dropCollection(TestDocument.class);

        TestDocument doc = new TestDocument();
        doc.setAge(42);
        doc.setName("Spiderman");
        doc.getAddress().setCity("Atlanta");
        doc.setCreated(new Date());
        mongo.save(doc);

        doc = new TestDocument();
        doc.setAge(69);
        doc.setName("Superman");
        doc.getAddress().setCity("New York");
        doc.getAddress().setState("NY");
        doc.setCreated(new Date());
        mongo.save(doc);
        
        for (int i = 0; i < 10; i++) {
            doc = new TestDocument();
            doc.setAge(100 + i);
            doc.setCreated(new Date());
            doc.setId(UUID.randomUUID().toString());
            doc.setName(doc.getId().substring(0, 12));
            TestAddress address = doc.getAddress();
            address.setState("FL");
            address.setPostal("12345");
            mongo.save(doc);
        }
    }

    @Test
    public void searchSingleIs() {
        SearchCriteria search = new SearchCriteria();
        search.setQuery("{name: 'Spiderman'}");

        SearchResponse<TestDocument> response = service.search(search, TestDocument.class);
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
        
        search.setQuery("{name: 'Slickman'}");
        response = service.search(search, TestDocument.class);
        assertNotNull(response);
        assertEquals(0, response.getResults().size());
    }
    
    @Test
    public void searchSingleRegex() {
        SearchCriteria search = new SearchCriteria();
        search.setQuery("{name: {$regex: 'Spider', $options: ''}}");

        SearchResponse<TestDocument> response = service.search(search, TestDocument.class);
        assertNotNull(response);
        assertEquals(1, response.getResults().size());

        search.setQuery("{name: {$regex: 'man', $options: ''}}");
        response = service.search(search, TestDocument.class);
        assertNotNull(response);
        assertEquals(2, response.getResults().size());
    }
    
    @Test
    public void searchTwoAttributes() {
        SearchCriteria search = new SearchCriteria();
        search.setQuery("{name: {'$regex': 'Spider', $options: ''}, age: {'$gt': 30}}");
        
        SearchResponse<TestDocument> response = service.search(search, TestDocument.class);
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
        assertEquals(42, response.getResults().get(0).getAge());

        search.setQuery("{name: {'$regex': 'man', $options: ''}, age: {'$gt': 30}}");
        response = service.search(search, TestDocument.class);
        assertNotNull(response);
        assertEquals(2, response.getResults().size());
    }
    
    @Test
    public void searchNestedAttribute() {
        SearchCriteria search = new SearchCriteria();
        search.setQuery("{address.city: 'Atlanta'}");
        
        SearchResponse<TestDocument> response = service.search(search, TestDocument.class);
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
        assertEquals("Atlanta", response.getResults().get(0).getAddress().getCity());
    }
}

package org.inigma.shared.search;

import static org.junit.Assert.*;

import java.util.Date;

import org.inigma.shared.search.SearchField.Operation;
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
    }

    @Test
    public void searchSingleRegex() {
        SearchCriteria<TestDocument> search = new SearchCriteria<TestDocument>(new TestDocument(), "name", "age",
                "address.city");
        search.getCriteria().setName("Spider");
        SearchField name = search.getField("name");
        name.setOperation(Operation.REGEX);

        SearchResponse<TestDocument> response = service.search(search);
        assertNotNull(response);
        assertEquals(1, response.getResults().size());

        search.getCriteria().setName("man");
        response = service.search(search);
        assertNotNull(response);
        assertEquals(2, response.getResults().size());
    }
    
    @Test
    public void searchTwoAttributes() {
        SearchCriteria<TestDocument> search = new SearchCriteria<TestDocument>(new TestDocument(), "name", "age",
                "address.city");
        search.getCriteria().setName("Spider");
        search.getCriteria().setAge(30);
        SearchField name = search.getField("name");
        name.setOperation(Operation.REGEX);
        SearchField age = search.getField("age");
        age.setOperation(Operation.GT);
        
        SearchResponse<TestDocument> response = service.search(search);
        assertNotNull(response);
        assertEquals(1, response.getResults().size());

        search.getCriteria().setName("man");
        response = service.search(search);
        assertNotNull(response);
        assertEquals(2, response.getResults().size());
    }
    
    @Test
    public void searchNestedAttribute() {
        SearchCriteria<TestDocument> search = new SearchCriteria<TestDocument>(new TestDocument(), "name", "age",
                "address.city");
        search.getCriteria().getAddress().setCity("Atlanta");
        SearchField city = search.getField("address.city");
        city.setOperation(Operation.IS);
        
        SearchResponse<TestDocument> response = service.search(search);
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
    }
}

package no.nb.microservices.catalogsearchwithin.core.index.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nb.microservices.catalogsearchwithin.core.index.repository.IndexRepository;

@RunWith(MockitoJUnitRunner.class)
public class IndexServiceImplTest {
    
    @Mock
    private IndexRepository indexRepository;
    
    @InjectMocks
    private IndexServiceImpl service;

    @Test
    public void test() {
        
        service.searchWithin("id1", "test");
        
        verify(indexRepository, times(1)).searchWithin("id1", "test");
    }

}

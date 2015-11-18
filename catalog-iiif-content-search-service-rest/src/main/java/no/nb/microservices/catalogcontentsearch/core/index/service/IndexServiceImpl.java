package no.nb.microservices.catalogcontentsearch.core.index.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nb.microservices.catalogcontentsearch.core.index.repository.IndexRepository;
import no.nb.microservices.catalogsearchindex.searchwithin.SearchWithinResource;

@Service
public class IndexServiceImpl implements IndexService {
    
    private IndexRepository indexRepository;

    @Autowired
    public IndexServiceImpl(IndexRepository indexRepository) {
        super();
        this.indexRepository = indexRepository;
    }

    @Override
    public SearchWithinResource searchWithin(String id, String q) {
        return indexRepository.searchWithin(id, q);
    }
    

}

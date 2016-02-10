package no.nb.microservices.catalogcontentsearch.core.search.service;

import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.commons.web.util.UserUtils;
import no.nb.commons.web.xforwarded.feign.XForwardedFeignInterceptor;
import no.nb.microservices.catalogcontentsearch.core.index.service.IndexService;
import no.nb.microservices.catalogcontentsearch.core.metadata.service.MetadataService;
import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import no.nb.microservices.catalogsearchindex.searchwithin.ContentSearchResource;

@Service
public class ContentSearchServiceImpl implements ContentSearchService {

    private IndexService indexService;
    private MetadataService metadataService;
    
    @Autowired
    public ContentSearchServiceImpl(IndexService indexService,
            MetadataService metadataService) {
        super();
        this.indexService = indexService;
        this.metadataService = metadataService;
    }

    @Override
    public ContentSearchResult search(String id, String q) {
        
        TracableId tracableId = new TracableId(Trace.currentSpan(), id, getSecurityInfo());
        
        Future<StructMap> struct = metadataService.getStructById(tracableId);
        Future<ContentSearchResource> contentSearchResult = indexService.contentSearch(tracableId, q);
        
        try {
            return new ContentSearchResult(struct.get(), contentSearchResult.get());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ContentSearchException("Failed searching for content for id " + id, ex);
        }
        
    }
    
    private SecurityInfo getSecurityInfo() {
        SecurityInfo securityInfo = new SecurityInfo();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        securityInfo.setxHost(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_HOST));
        securityInfo.setxPort(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_PORT));
        securityInfo.setxRealIp(UserUtils.getClientIp(request));
        securityInfo.setSsoToken(UserUtils.getSsoToken(request));
        return securityInfo;
    }
    

}

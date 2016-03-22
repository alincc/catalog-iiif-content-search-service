package no.nb.microservices.catalogcontentsearch.core.search.service;

import no.nb.commons.web.util.UserUtils;
import no.nb.commons.web.xforwarded.feign.XForwardedFeignInterceptor;
import no.nb.microservices.catalogcontentsearch.core.index.service.IndexService;
import no.nb.microservices.catalogcontentsearch.core.search.ContentSearchException;
import no.nb.microservices.catalogcontentsearch.core.search.ContentSearchResult;
import no.nb.microservices.catalogcontentsearch.core.search.SecurityInfo;
import no.nb.microservices.catalogcontentsearch.core.search.TracableId;
import no.nb.microservices.catalogsearchindex.searchwithin.ContentSearchResource;
import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Future;

@Service
public class ContentSearchServiceImpl implements ContentSearchService {

    private IndexService indexService;

    @Autowired
    public ContentSearchServiceImpl(IndexService indexService) {
        this.indexService = indexService;
    }

    @Override
    public ContentSearchResult search(String id, String q) {
        
        TracableId tracableId = new TracableId(Trace.currentSpan(), id, getSecurityInfo());
        
        Future<ContentSearchResource> contentSearchResult = indexService.contentSearch(tracableId, q);
        
        try {
            return new ContentSearchResult(contentSearchResult.get());
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

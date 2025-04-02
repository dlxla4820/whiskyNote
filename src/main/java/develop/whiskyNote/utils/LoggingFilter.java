package develop.whiskyNote.utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Order(1)
public class LoggingFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
      chain.doFilter(request, response);
      return;
    }
    //Spring에서 자동으로 Request cacheing 처리
    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

    long startTime = System.currentTimeMillis();
    chain.doFilter(wrappedRequest, wrappedResponse);

    String payload = extractRequestPayload(wrappedRequest);
    logger.info("[Request] Method: {}, URL: {}, Payload: {}",
        wrappedRequest.getMethod(),
        wrappedRequest.getRequestURI(),
        payload);

    long duration = System.currentTimeMillis() - startTime;
    logger.info("[Response] Status: {}, Duration: {}ms",
        wrappedResponse.getStatus(), duration);

    wrappedResponse.copyBodyToResponse(); // 꼭 필요함!
  }

  private String extractRequestPayload(ContentCachingRequestWrapper request) {
    String payload = null;
    try {
      byte[] buf = request.getContentAsByteArray();
      if (buf.length > 0) {
        payload = new String(buf, 0, buf.length, request.getCharacterEncoding());
      } else if (!request.getParameterMap().isEmpty()) {
        // Body가 없을 경우 파라미터 맵을 문자열로 변환
        StringBuilder paramBuilder = new StringBuilder();
        request.getParameterMap().forEach((key, values) -> {
          for (String value : values) {
            if (paramBuilder.length() > 0) {
              paramBuilder.append("&");
            }
            paramBuilder.append(key).append("=").append(value);
          }
        });
        payload = paramBuilder.toString();
      } else {
        payload = "";
      }
    } catch (UnsupportedEncodingException ex) {
      payload = "[Unsupported Encoding]";
    }
    return payload;
  }

}

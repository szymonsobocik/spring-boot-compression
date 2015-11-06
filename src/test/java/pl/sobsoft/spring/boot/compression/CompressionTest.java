package pl.sobsoft.spring.boot.compression;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CompressionSampleApp.class)
@WebIntegrationTest(randomPort = true)
public class CompressionTest {

    @Value("${local.server.port}")
    protected int port;
    private static final String HOST_URL = "http://localhost";
    private TestRestTemplate restTemplate;

    private URI url(String endpoint) {
        try {
            return new URI(HOST_URL + ":" + port + endpoint);
        } catch (URISyntaxException e) {
            //yeah, don't do it really
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() throws Exception {
        restTemplate = new TestRestTemplate();
    }


    //This test fails, but it should not. Why contentEncodingHeaders is null?
    @Test
    public void shouldHaveContentEncodingHeaderWhenUserAgentSetToAppleWebKit() {
        //given
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "gzip,deflate");
        headers.add("User-Agent", "AppleWebKit");

        final HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        //when
        final ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url("/"), HttpMethod.GET, requestEntity, byte[].class);

        //then
        final List<String> contentEncodingHeaders = responseEntity.getHeaders().get("Content-Encoding");

        //this line fails !
        assertNotNull(contentEncodingHeaders);
        assertEquals(contentEncodingHeaders.size(), 1);
        final String header = contentEncodingHeaders.get(0);
        assertEquals(header, "gzip");
    }

    @Test
    public void shouldRespondSomething() {
        //when
        final ResponseEntity<SomeDTO> responseEntity = restTemplate.getForEntity(url("/"), SomeDTO.class);

        //then
        final SomeDTO body = responseEntity.getBody();
        assertEquals(body.getSomething(), "Something");
    }

    @Test
    public void shouldHaveContentEncodingHeader() {
        //given
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "gzip,deflate");

        final HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        //when
        final ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url("/"), HttpMethod.GET, requestEntity, byte[].class);

        //then
        final List<String> contentEncodingHeaders = responseEntity.getHeaders().get("Content-Encoding");
        assertEquals(contentEncodingHeaders.size(), 1);
        final String header = contentEncodingHeaders.get(0);
        assertEquals(header, "gzip");
    }

    @Test
    public void shouldHaveContentEncodingHeaderWhenUserAgentSet() {
        //given
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "gzip,deflate");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) Chrome/46.0.2490.80 Safari/537.36");

        final HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        //when
        final ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url("/"), HttpMethod.GET, requestEntity, byte[].class);

        //then
        final List<String> contentEncodingHeaders = responseEntity.getHeaders().get("Content-Encoding");
        assertEquals(contentEncodingHeaders.size(), 1);
        final String header = contentEncodingHeaders.get(0);
        assertEquals(header, "gzip");
    }

    @Test
    public void shouldHaveContentEncodingHeaderWhenUserAgentSetToUnknownBrowser() {
        //given
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "gzip,deflate");
        headers.add("User-Agent", "SomeUnknownBrowser");

        final HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        //when
        final ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url("/"), HttpMethod.GET, requestEntity, byte[].class);

        //then
        final List<String> contentEncodingHeaders = responseEntity.getHeaders().get("Content-Encoding");
        assertEquals(contentEncodingHeaders.size(), 1);
        final String header = contentEncodingHeaders.get(0);
        assertEquals(header, "gzip");
    }
}

package com.thoughtworks.jimmy.controller;

import com.thoughtworks.jimmy.SpringBootWebApplication;
import com.thoughtworks.jimmy.model.Book;
import com.thoughtworks.jimmy.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
public class BookShelfControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private BookService bookService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        reset(bookService);
    }


    @Test
    public void should_return_single_books_when_given_isbn_existed() throws Exception {
        when(bookService.findByIsbn(anyString()))
                .thenReturn((new Book("1234", "1234", "1234", 12.0)));

        mockMvc.perform(get("/book/1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", is("1234")))
                .andExpect(jsonPath("$.name", is("1234")))
                .andExpect(jsonPath("$.author", is("1234")))
                .andExpect(jsonPath("$.price", is(12.0)));
    }

    @Test
    public void should_return_404_when_given_isbn_not_existed() throws Exception {
        when(bookService.findByIsbn(anyString()))
                .thenReturn(null);

        mockMvc.perform(get("/book/1234"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_books_when_ask_all_info() throws Exception {
        when(bookService.findAll())
                .thenReturn(Arrays.asList(new Book("1234", "1234", "1234", 12.0)));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is("1234")))
                .andExpect(jsonPath("$[0].name", is("1234")))
                .andExpect(jsonPath("$[0].author", is("1234")))
                .andExpect(jsonPath("$[0].price", is(12.0)));
    }

    @Test
    public void should_return_latest_book_info_when_create_book_given_book_info_not_existed() throws Exception {
        Book testBook = new Book("1234", "1234", "1234", 12.0);
        when(bookService.findAll())
                .thenReturn(Arrays.<Book>asList(testBook));

        mockMvc.perform(post("/book/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json(testBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].isbn", is("1234")))
                .andExpect(jsonPath("$[0].name", is("1234")))
                .andExpect(jsonPath("$[0].author", is("1234")))
                .andExpect(jsonPath("$[0].price", is(12.0)));
    }

    @Test
    public void should_return_conflict_when_creating_book_but_already_existed() throws Exception {
        Book testBook = new Book("1234", "1234", "1234", 12.0);
        when(bookService.findByIsbn(anyString()))
                .thenReturn(testBook);

        mockMvc.perform(post("/book/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(testBook)))
                .andExpect(status().isConflict());
    }

    @Test
    public void should_return_changed_info_when_given_book_existed() throws Exception {
        Book testBook = new Book("1234", "1234", "1234", 12.0);

        mockMvc.perform(put("/book/" + testBook.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(testBook)))
                .andExpect(status().isOk());

        verify(bookService, times(1)).edit(Matchers.any());
    }

    @Test
    public void should_return_argument_error_when_given_book_not_existed() throws Exception {
        Book testBook = new Book("1234", "1234", "1234", 12.0);
        when(bookService.findByIsbn(anyString()))
                .thenReturn(testBook);

        mockMvc.perform(put("/book/" + testBook.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(testBook)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).edit(Matchers.any());
    }

    @Test
    public void should_return_200_when_given_delete() throws Exception {
        Book testBook = new Book("1234", "1234", "1234", 12.0);

        mockMvc.perform(delete("/book/" + testBook.getIsbn()))
                .andExpect(status().isOk());
    }


    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
package com.exam.todojpa.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.exam.todojpa.config.ApplicationConfig;
import com.exam.todojpa.config.MvcConfig;
import com.exam.todojpa.domain.Todo;
import com.exam.todojpa.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, ApplicationConfig.class})
public class TodoControllerTest {
	
	private final List<Todo> todos = new ArrayList();
	private Todo postTodo;
	
	@InjectMocks
	private TodoAPIController todoAPIController;
	
	@Mock
	private TodoService todoService;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void initTodos() {
		Todo todo = new Todo();
		todo.setId(1L);
		todo.setTodo("hello");
		todo.setDone(false);
		
		todos.add(todo);
		
		postTodo = new Todo();
		postTodo.setId(2L);
		postTodo.setTodo("hoho");
		postTodo.setDone(false);
		
		
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(todoAPIController).build();
	}
	
	@Test
	public void list() throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		
		when(todoService.getTodos()).thenReturn(todos);
		
		this.mockMvc.perform(get("/api/todos"))
		.andExpect(status().isOk())
		.andDo(print());
	
		
		verify(todoService, times(1)).getTodos();
	}

	@Test
	public void testAddtod() throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		when(todoService.addTodo(any())).thenReturn(postTodo);
		
		Todo requestBodyTodo = new Todo();
		requestBodyTodo.setTodo("hello");
		String content = objectMapper.writeValueAsString(requestBodyTodo);
		
		this.mockMvc.perform(post("/api/todos").content(content).
				contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print());
		
		verify(todoService, times(1)).addTodo(any());
		
	}
}

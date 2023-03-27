package co.sohamds.spring.todo.controllers;

import co.sohamds.spring.todo.SpringBootTodoAppApplication;
import io.opentelemetry.api.trace.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.sohamds.spring.todo.domain.Todo;
import co.sohamds.spring.todo.repository.TodoRepository;

@Controller
public class TodoController {
	@Autowired
	TodoRepository todoRepository;
	
	@GetMapping
	public String index() {
	return "index.html";
}

@GetMapping("/todos")
public String todos(Model model) {

		Span span = SpringBootTodoAppApplication.tracer.spanBuilder("todos").startSpan();

model.addAttribute("todos", todoRepository.findAll());

span.end();

return "todos";
}

@PostMapping("/todoNew")
public String add(@RequestParam String todoItem, @RequestParam
	String status, Model model) {

	Span span = SpringBootTodoAppApplication.tracer.spanBuilder("Add").startSpan();


	Todo todo = new Todo(todoItem, status);
	span.addEvent("Creating new todo");
	todo.setTodoItem(todoItem);
	todo.setCompleted(status);
	todoRepository.save(todo);
	span.addEvent("Saving todo to repository");
	model.addAttribute("todos", todoRepository.findAll());

	span.end();

	return "redirect:/todos";


}

@PostMapping("/todoDelete/{id}")
public String delete(@PathVariable long id, Model model) {

	Span span = SpringBootTodoAppApplication.tracer.spanBuilder("Delete").startSpan();

	todoRepository.deleteById(id);
	model.addAttribute("todos", todoRepository.findAll());

	span.end();

	return "redirect:/todos";


}

@PostMapping("/todoUpdate/{id}")
public String update(@PathVariable long id, Model model) {

	Span span = SpringBootTodoAppApplication.tracer.spanBuilder("update").startSpan();

	Todo todo = todoRepository.findById(id).get();
	span.addEvent("Getting todo ID from repository");
	if("Yes".equals(todo.getCompleted())) {
	todo.setCompleted("No");
	span.addEvent("Setting status to no");
	}
	else {
	todo.setCompleted("Yes");
	span.addEvent("Setting status to Yes");
	}
	todoRepository.save(todo);
	model.addAttribute("todos", todoRepository.findAll());
	span.addEvent("Saving update and finishing method");

	span.end();

	return "redirect:/todos";
}
}
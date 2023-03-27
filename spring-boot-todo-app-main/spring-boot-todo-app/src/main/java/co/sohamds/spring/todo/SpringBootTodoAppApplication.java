package co.sohamds.spring.todo;

import java.util.Arrays;
import java.util.List;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import co.sohamds.spring.todo.domain.Todo;
import co.sohamds.spring.todo.repository.TodoRepository;



import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import io.opentelemetry.api.common.Attributes;
import org.springframework.data.geo.Metric;


@SpringBootApplication
public class SpringBootTodoAppApplication implements CommandLineRunner
{

	public static OpenTelemetry openTelemetry;
	public static Tracer tracer;


@Autowired
public TodoRepository todoRepository;
public static void main(String[] args) {





	Resource resource = Resource.getDefault().merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "logical-service-name")));

	SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
			.addSpanProcessor(SimpleSpanProcessor.create(OtlpGrpcSpanExporter.getDefault()))
			.setResource(resource)
			.build();




	openTelemetry = OpenTelemetrySdk.builder()
			.setTracerProvider(sdkTracerProvider)
			.build();



	tracer = openTelemetry.getTracer("spring-boot-todo-app", "1.0.0");

	SpringApplication.run(SpringBootTodoAppApplication.class, args);

}

@Override
public void run(String... args) throws Exception {
	
	Todo test=Todo.builder().id(10).completed("its completed").todoItem("python ML").build();
	System.out.println(test.toString());
	List<Todo> todos = Arrays.asList(new Todo("Learn Spring" , "Yes"), new Todo("Learn Driving", "No"), new Todo("Go for a Walk", "No"), new Todo("Cook Dinner", "Yes"));
todos.forEach(todoRepository::save);



}
}

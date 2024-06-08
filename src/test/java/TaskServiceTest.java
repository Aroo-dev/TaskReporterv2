import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.mwo.java1.logger.Logger;
import pl.edu.agh.mwo.java1.model.Project;
import pl.edu.agh.mwo.java1.repository.TaskRepository;
import pl.edu.agh.mwo.java1.service.TaskService;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;
public class TaskServiceTest {

    private TaskRepository taskRepository;
    private Logger logger;
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskRepository = mock(TaskRepository.class);
        logger = mock(Logger.class);
        when(taskRepository.loadProjects()).thenReturn(List.of(new Project("Project1"), new Project("Project2")));
        taskService = new TaskService(taskRepository, logger);
    }

    @Test
    public void testListProjects() {
        taskService.listProjects();
        verify(logger).logInfo("Project1");
        verify(logger).logInfo("Project2");
    }

    @Test
    public void testListProjectsWhenNoProjects() {
        when(taskRepository.loadProjects()).thenReturn(new ArrayList<>());
        taskService = new TaskService(taskRepository, logger);

        taskService.listProjects();
        verify(logger, never()).logInfo(anyString());
    }
}

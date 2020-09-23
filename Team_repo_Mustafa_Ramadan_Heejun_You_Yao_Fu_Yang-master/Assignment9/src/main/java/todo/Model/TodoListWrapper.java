package todo.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import todo.Controller.Util.CSVParser;
import todo.Controller.Util.InvalidInputCombinationException;

/**
 * This class is an internal data structure to hold the To-do objects,
 * and is a master copy of our data, serves as a model in MVC architecture.
 * We only allow one instance of this master copy, hence singleton pattern is applied.
 */
public class TodoListWrapper {
  private static TodoListWrapper instance;
  protected List<Todo> todoList;

  /**
   * This constructor creates a new ArrayList as it todo list
   */
  private TodoListWrapper() {
    this.todoList = new ArrayList<>();
  }

  /**
   * This method creates a lazy allocation of the TodoListWrapper instance which ensures that only
   * one instance of the Object  TodoListWrapper occurs at a time.
   * @return a TodoListWrapper object called instance
   */
  public static synchronized TodoListWrapper getInstance() {
    if (instance == null) {
      instance = new TodoListWrapper();
    }
    return instance;
  }

  /**
   * Getter method to retrive the TodoList upon wrapper.getTodoList()
   * @return
   */
  public List<Todo> getTodoList() {
    return todoList;
  }

  /**
   * When user prompts --add command for the first time ever, this method adds the TodoObject by
   * calling the addTodo() method.
   * @param allLinesFromFile is a List of strings that are assessed as parsed tokens
   * @throws InvalidInputCombinationException
   */
  public void loadTodo(List<String> allLinesFromFile) throws InvalidInputCombinationException {
    if (allLinesFromFile != null) {
      for (String line : allLinesFromFile.subList(1, allLinesFromFile.size())) {
        // make todoObject out of the string line
        ArrayList<String> tokenArray = this.loadTodoHelper(line);
        // assume the cvs column names will not change(be in fixed order): id, text, completed, due, priority, category
        Todo todoObject = new Todo(tokenArray.get(1), Boolean.parseBoolean(tokenArray.get(2)),
            tokenArray.get(3), tokenArray.get(4), tokenArray.get(5));
        this.addTodo(todoObject);
      }
    }
    // if allLinesFromFile IS null, which happens when CSVFileReader didn't locate any file,
    // this method does nothing
  }

  /**
   * This helper method aids the loadTodo method by processing the individual tokens if they have
   * "?" with null. Now, the newly formatted array can be loaded properly with these accessible
   * processable tokens in the LoadTodo methos
   * @param line is the ling in the array that gets assessed by the CSVParser.ParseCSVLine()
   * @return a formatted array list of strings that have these new processable tokens
   */
  private ArrayList<String> loadTodoHelper(String line) {
    ArrayList<String> formattedArray = new ArrayList<>();
    String[] tokenArray = CSVParser.ParseCSVLine(line);
   for (String token : tokenArray) {
      if (token.equals("?")) {
        formattedArray.add(null);
      } else {
        formattedArray.add(token);
      }
    }
    return formattedArray;
  }

  /**
   * This method gets called when the user prompts --add command so that the TodoObject can get
   * added to the list of Todos
   * @param todoObject that gets added to the list of Todos
   */
  public void addTodo(Todo todoObject) {
    // every time i "add" to the list, give todoObject an ID.
    todoList.add(todoObject);
    Integer count = todoList.size();
    todoObject.setId(count);
  }

  /**
   * This method gets called when the user prompts --complete command with id. Now the unique
   * TodoObject can be set as complete
   * @param id will be passed in as an integer to locate the unique TodoObject in the list
   */
  public void completeTodo(Integer id) {
    // so (id - 1) is going to be the index of that todoObject in todoList
    if (id > todoList.size()) {
      System.out.println("You have provided invalid ID, can't update");
    } else if (todoList.get(id - 1).isCompleted()) {
      System.out.println("Todo with ID #" + id + " is already marked completed");
    } else {
      todoList.get(id - 1).setCompleted();
    }
  }

  /**
   * Returns true if the object being compared is equal to this object; false otherwise.
   * @param o - The other object being compared.
   * @return true if the object being compared is equal to this object; false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TodoListWrapper that = (TodoListWrapper) o;
    return Objects.equals(todoList, that.todoList);
  }

  /**
   * Returns a hashCode representation  of this object.
   * @return a hashCode representation  of this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(todoList);
  }

  /**
   * Returns a String representation of the object.
   * @return a String representation of the object.
   */
  @Override
  public String toString() {
    String listToString = "";
    for (Todo todo : todoList) {
      listToString += (todo.toString() + System.lineSeparator());
    }
    return listToString;
  }

}

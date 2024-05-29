import java.io.*;
import java.util.List;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {


        // Check all input arguments

        String input = null;
        try {
            input = args[0];
        } catch (IndexOutOfBoundsException ignored) {
        }

        String output = null;
        try {
            output = args[1];
        } catch (IndexOutOfBoundsException ignored) {
        }

        String treeOrNot = null;
        try {
            treeOrNot = args[2];
        } catch (IndexOutOfBoundsException ignored) {
        }


        FileGateway gateway = new FileGatewayImpl();

        // We instantiate the proper interactor depending on the use case

        Function<String, Result<List<String>>> interactor =
                (treeOrNot == null) ?
                        new ListOrderedByNameInteractor(gateway::getAllFiles) :
                        new GetFilesystemTreeInteractor();


        // If the output mode is CONSOLE we print the output of the interactor
        // If the output mode is a file we print the output in that file

        if (output.equals("CONSOLE")) {
            interactor.apply(input).getOrElse(List.of("Empty")).forEach(System.out::println);
        } else {
            new WriteFilesystemToFileInteractor(gateway::writeTreeToFile, interactor).apply(input, output);
        }

        // BONUS TRACK AKA EXERCISE 5

        System.out.println("Serialization exercise:");
        for (int i = 0; i < 5; i++) System.out.println();

        Function<Object, Result<Boolean>> serializeFunction = something -> {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/Serialized.ser"))) {
                out.writeObject(something);
                return Result.success(true);
            } catch (IOException i) {
                return Result.failure(new RuntimeException(i.getMessage(), i));
            }
        };

        Function<String, Result<Object>> deserializeFunction = file -> {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                return Result.success(in.readObject());
            } catch (IOException | ClassNotFoundException i) {
                return Result.failure(new RuntimeException(i.getMessage(), i));
            }
        };

        Person antonio = new Person("Antonio", 34);
        serializeFunction.apply(antonio);

        Person p = (Person) deserializeFunction.apply("/home/miguel/IdeaProjects/s105_2/src/Serialized.ser")
                .getOrElse(new Person("Faliure", 0));


        System.out.println(p.equals(antonio));


    }

    private record Person(String name, int age) implements Serializable{
    }

}
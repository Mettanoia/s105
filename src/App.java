import file_gateway.FileGateway;
import file_gateway.FileGatewayImpl;
import file_gateway.Result;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import use_cases.GetFilesystemTreeInteractor;
import use_cases.ListOrderedByNameInteractor;
import use_cases.WriteFilesystemToFileInteractor;

import java.io.*;
import java.util.List;
import java.util.function.Function;

public class App {

    public static <PropertiesConfiguration> void run(String[] args) {

        // Read app.config.properties configuration file

        Configurations configs = new Configurations();
        PropertiesConfiguration config;
        try {
            config = (PropertiesConfiguration) configs.properties(new File("app.config.properties"));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }


        // Check all input arguments

        String[] params = getParameters((org.apache.commons.configuration2.PropertiesConfiguration) config, args);
        String input = params[0];
        String output = params[1];
        String treeOrNot = params[2];

        FileGateway gateway = new FileGatewayImpl();

        // We instantiate the proper interactor depending on the use case

        Function<String, Result<List<String>>> interactor =
                (Boolean.parseBoolean(treeOrNot)) ?
                        new GetFilesystemTreeInteractor() :
                        new ListOrderedByNameInteractor(gateway::getAllFiles);


        // If the output mode is CONSOLE we print the output of the interactor
        // If the output mode is a file we print the output in that file

        if (output.equals("CONSOLE")) {
            interactor.apply(input).getOrElse(List.of("Empty")).forEach(System.out::println);
        } else {
            try {
                new WriteFilesystemToFileInteractor(gateway::writeTreeToFile, interactor).apply(input, output);
            } catch (Exception e) {

            }
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

    private static String[] getParameters(PropertiesConfiguration config, String[] args) {

        String input = null;
        try {
            input = args[0];
        } catch (IndexOutOfBoundsException ignored) {
            input = config.getString("inputDir");
        }

        String output = null;
        try {
            output = args[1];
        } catch (IndexOutOfBoundsException ignored) {
            output = config.getString("outputDir");
        }

        String treeOrNot = null;
        try {
            treeOrNot = args[2];
        } catch (IndexOutOfBoundsException ignored) {
            treeOrNot = config.getString("isTree");
        }

        return new String[]{input, output, treeOrNot};

    }

    private record Person(String name, int age) implements Serializable {
    }

}
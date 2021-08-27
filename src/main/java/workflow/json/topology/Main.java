package workflow.json.topology;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;

/*
*  Project created for explain how to serialize/deserialize a map with cyclic dependency objects
*  The library used is jackson
*/

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        HashMap<String, WorkflowNode> map = new HashMap<>();

        WorkflowNode n1 = WorkflowNode.builder()
                .identifier("N1")
                .label("N1")
                .namespace("NAMESPACE")
                .service("SERVICE")
                .version("1.0.0")
                .function("FUNCTION")
                .parents(new LinkedList<>())
                .children(new LinkedList<>())
                .build();

        WorkflowNode n2 = WorkflowNode.builder()
                .identifier("N2")
                .label("N2")
                .namespace("NAMESPACE")
                .service("SERVICE")
                .version("1.0.0")
                .function("FUNCTION")
                .parents(new LinkedList<>())
                .children(new LinkedList<>())
                .build();

        WorkflowNode n3 = WorkflowNode.builder()
                .identifier("N3")
                .label("N3")
                .namespace("NAMESPACE")
                .service("SERVICE")
                .version("1.0.0")
                .function("FUNCTION")
                .parents(new LinkedList<>())
                .children(new LinkedList<>())
                .build();

        WorkflowNode n4 = WorkflowNode.builder()
                .identifier("N4")
                .label("N4")
                .namespace("NAMESPACE")
                .service("SERVICE")
                .version("1.0.0")
                .function("FUNCTION")
                .parents(new LinkedList<>())
                .children(new LinkedList<>())
                .build();

        n1.getChildren().add(n2);
        n1.getChildren().add(n3);

        n2.getParents().add(n1);
        n3.getParents().add(n1);

        n2.getChildren().add(n4);
        n3.getChildren().add(n4);

        n4.getParents().add(n2);
        n4.getParents().add(n3);

        map.put(n1.getIdentifier(), n1);
        map.put(n2.getIdentifier(), n2);
        map.put(n3.getIdentifier(), n3);
        map.put(n4.getIdentifier(), n4);

        try {
            ObjectMapper mapper = new ObjectMapper();

            Path filePath = Paths.get("output.txt");
            if (Files.exists(filePath)) Files.delete(filePath);

            // Serialize tree object map
            Files.write(
                    filePath,
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(map),
                    new StandardOpenOption[]{
                            StandardOpenOption.CREATE,
                            StandardOpenOption.CREATE_NEW,
                            StandardOpenOption.WRITE
                    }
            );

            // Deserialize tree object map and construct all cyclic reference
            TypeReference<HashMap<String, WorkflowNode>> typeRef = new TypeReference<HashMap<String, WorkflowNode>>() {};
            HashMap<String, WorkflowNode> map2 = mapper.readValue(Files.readAllBytes(filePath), typeRef);

            // Check if the content extracted from file it's equals to original
            String originalSerializedContent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            String serializedContentAfterDeserialize = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map2);

            if (!serializedContentAfterDeserialize.equals(originalSerializedContent)){
                throw new Exception("The content must be equals!!");
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

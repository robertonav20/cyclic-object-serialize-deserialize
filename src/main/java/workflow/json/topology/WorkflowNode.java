package workflow.json.topology;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "identifier", scope = String.class)
public class WorkflowNode {
    private String identifier;
    private String label;
    private String namespace;
    private String service;
    private String version;
    private String function;

    @JsonIdentityReference(alwaysAsId = true)
    private List<WorkflowNode> parents;

    @JsonIdentityReference(alwaysAsId = true)
    private List<WorkflowNode> children;

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }
}



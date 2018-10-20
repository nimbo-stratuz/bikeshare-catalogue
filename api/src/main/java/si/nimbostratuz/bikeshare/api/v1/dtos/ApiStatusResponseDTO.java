package si.nimbostratuz.bikeshare.api.v1.dtos;

import lombok.Data;

import javax.ws.rs.core.Response;

@Data
public class ApiStatusResponseDTO {

    private Integer status;
    private String message;

    public ApiStatusResponseDTO(Response.Status status, String message) {
        this.status = status.getStatusCode();
        this.message = message;
    }
}

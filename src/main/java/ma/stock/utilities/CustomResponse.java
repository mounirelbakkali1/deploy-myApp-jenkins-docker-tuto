package ma.stock.utilities;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CustomResponse<T> {
    private String message ;
    private T data ;
}

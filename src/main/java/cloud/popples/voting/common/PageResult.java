package cloud.popples.voting.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResult <T> implements Serializable {

    private final static long serialVersionUID = 1L;

    private final static int SUCCESS_CODE = 0;

    private final static String SUCCESS_MSG = "success";

    private Integer code;

    private String message;

    private Integer pageNo;

    private Integer pageSize;

    private Integer pageTotal;

    private Iterable<T> pageData;

    public static <T> PageResult<T> success(Page<T> page) {
        return PageResult.<T>builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MSG)
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
                .pageTotal(page.getTotalPages())
                .pageData(page.getContent())
                .build();
    }

}

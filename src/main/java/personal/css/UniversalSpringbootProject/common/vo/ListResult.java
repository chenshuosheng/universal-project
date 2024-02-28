package personal.css.UniversalSpringbootProject.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 用于封装结果列表
 * @Author: CSS
 * @Date: 2024/2/28 23:44
 */
public class ListResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long totalCount;

    private List<T> items;

    public ListResult() {
    }

    public ListResult(long totalCount, List<T> items) {
        this.totalCount = totalCount;
        this.items = items;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}

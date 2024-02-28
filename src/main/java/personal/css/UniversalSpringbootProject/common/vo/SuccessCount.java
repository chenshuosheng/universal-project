package personal.css.UniversalSpringbootProject.common.vo;

import java.io.Serializable;

/**
 * @Description: 成功数目
 * @Author: CSS
 * @Date: 2024/2/28 23:44
 */
public class SuccessCount implements Serializable {
    private static final long serialVersionUID = 1L;

    private int successCount;

    public SuccessCount() {
    }

    public SuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
}

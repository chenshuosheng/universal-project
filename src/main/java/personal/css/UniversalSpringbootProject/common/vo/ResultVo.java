package personal.css.UniversalSpringbootProject.common.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: 用于封装结果信息
 * @Author: CSS
 * @Date: 2024/2/28 23:44
 */
public class ResultVo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否成功，true：成功，false：失败")
    private boolean success;

    @ApiModelProperty(value = "错误信息，当success为false才有")
    private String error;

    @ApiModelProperty(value = "结果集")
    private T result;

    public ResultVo() {
    }

    public ResultVo(boolean success, String error, T result) {
        this.success = success;
        this.error = error;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

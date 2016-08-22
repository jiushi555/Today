package xml.org.today.nohttp;

import com.alibaba.fastjson.JSON;



public class Requests  {
	String code;
	String message;
	String data;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 你的{@link E}必须提供默认无参构造
	 * 
	 * @param clazz
	 * @return
	 */
	public <E> E parseData(Class<E> clazz) {
		E e = null;
		try {
			e = JSON.parseObject(getData(), clazz);
		} catch (Exception e1) {
			try {
				e = clazz.newInstance();
			} catch (Exception e2) {
			}
		}
		return e;
	}

}

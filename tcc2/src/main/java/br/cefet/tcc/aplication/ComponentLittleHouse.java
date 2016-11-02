package br.cefet.tcc.aplication;

public class ComponentLittleHouse {
	private String path;
	private String component;
	private String valueComponents; // nao foram tratados nesse trabalho
	
	public ComponentLittleHouse(String path, String component, String valueComponents) {
		this.path = path;
		this.component = component;
		this.valueComponents = valueComponents;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getValueComponents() {
		return valueComponents;
	}
	public void setValueComponents(String valueComponents) {
		this.valueComponents = valueComponents;
	}
	
	
}

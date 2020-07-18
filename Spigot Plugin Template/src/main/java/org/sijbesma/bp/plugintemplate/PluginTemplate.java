package org.sijbesma.bp.plugintemplate;

import org.bukkit.plugin.java.JavaPlugin;
import org.sijbesma.bp.utils.DebugLogger;

public class PluginTemplate extends JavaPlugin{

	private static JavaPlugin INSTANCE;
	private static boolean debugFlag;
	private static DebugLogger debugLogger;
	
	public PluginTemplate() {
		PluginTemplate.INSTANCE = this;
		debugLogger = new DebugLogger(this.getLogger(),debugFlag);
	}
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onDisable() {
		
	}
}

package io.github.randommcsomethin.explorerssuite.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "explorerssuite")
public class ExplorersSuiteConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("explorerssuite.general")
    public transient static ExplorersSuiteConfig instance;

    @ConfigEntry.Category("explorerssuite.general")
    @ConfigEntry.Gui.Tooltip
    public boolean campfiresPlaceUnlit = true;

    @ConfigEntry.Category("explorerssuite.general")
    @ConfigEntry.Gui.Tooltip
    public boolean campfiresHealPlayers = true;

    @ConfigEntry.Category("explorerssuite.general")
    @ConfigEntry.Gui.Tooltip
    public boolean campfiresHealPassives = true;

    @ConfigEntry.Category("explorerssuite.general")
    @ConfigEntry.Gui.Tooltip
    public boolean dropLadders = true;

    @ConfigEntry.Category("explorerssuite.general")
    @ConfigEntry.Gui.Tooltip
    public boolean pigsDropTallow = true;

    @ConfigEntry.Category("explorerssuite.general")
    @ConfigEntry.Gui.Tooltip
    public int dayColor = 0xFFEEAA;

    @ConfigEntry.Category("explorerssuite.general")
    @ConfigEntry.Gui.Tooltip
    public int nightColor = 0xCCAAFF;

    @ConfigEntry.Category("explorerssuite.general")
    @ConfigEntry.Gui.Tooltip
    public boolean twelveHourFormat = true;
}
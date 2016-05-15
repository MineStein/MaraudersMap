package me.tylergrissom.maraudersmap.action;

import com.elmakers.mine.bukkit.action.BaseSpellAction;
import com.elmakers.mine.bukkit.api.action.CastContext;
import com.elmakers.mine.bukkit.api.spell.SpellResult;
import me.tylergrissom.maraudersmap.Main;
import me.tylergrissom.maraudersmap.inventory.MapInventory;
import org.bukkit.entity.Player;

/**
 * Copyright (c) 2013-2016 Tyler Grissom
 */
public class MaraudersMapAction extends BaseSpellAction {

    @Override
    public SpellResult perform(CastContext castContext) {
        Player player = (Player) castContext.getTargetEntity();

        new MapInventory(Main.staticPlugin).open(player);

        return SpellResult.CAST_SELF;
    }
}

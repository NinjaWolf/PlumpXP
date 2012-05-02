package com.github.barneygale.PlumpXP;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlumpXPListener implements Listener {
	public final PlumpXP plugin;
	public PlumpXPListener(PlumpXP instance) {
		plugin = instance;
	}
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		 Entity target = event.getEntity();
	     Player attacker = getAttacker(target.getLastDamageCause());
	     int xp = event.getDroppedExp();
	     
		 if(event instanceof PlayerDeathEvent) {
	            handlePlayerDeath((PlayerDeathEvent) event,attacker, xp);
	            return;
	     }
	
		 //not killed by player
	     if(attacker == null) {
	            return;
	        }
	     	handleMonsterDeath(event, attacker, xp);
		}
	
	private void handleMonsterDeath(EntityDeathEvent event, Player attacker, int exp) {
		EntityType target = event.getEntityType();
				
		if (target == EntityType.BLAZE) {
			exp *= plugin.config.BLAZE_MULTIPLIER;
		}
		else {
			exp *= plugin.config.PLUMP_MULTIPLIER;
		}
		event.setDroppedExp(exp);
	}
	
	private void handlePlayerDeath(PlayerDeathEvent event,Player attacker, int exp) {
		Player target = (Player)event.getEntity();	
		Player killer = getAttacker(target.getLastDamageCause());
		//killed by another player
		if (killer != null) {
			exp *= plugin.config.PLAYER_MULTIPLIER;
		}
		event.setDroppedExp(exp);
	}
	
	private Player getAttacker(EntityDamageEvent attacker) {
        if((attacker == null) || (!(attacker instanceof EntityDamageByEntityEvent))){
            return null;
        }
   
        Player p = null;
        Entity damager = ((EntityDamageByEntityEvent) attacker).getDamager();

        if(damager instanceof Arrow) {
            Arrow damage_arrow = (Arrow) damager;
            if(damage_arrow.getShooter() instanceof Player) {
                p = (Player) damage_arrow.getShooter();
            }
        } else if(damager instanceof Player) {
            p = (Player)damager;
        }
        return p;
    }
	
}
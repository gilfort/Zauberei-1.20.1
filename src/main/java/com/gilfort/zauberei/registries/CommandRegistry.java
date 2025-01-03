package com.gilfort.zauberei.registries;



import com.gilfort.zauberei.helpers.CommandHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber()
public class CommandRegistry {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event){
        CommandHandler.registerCommands(event.getDispatcher());
    }

}

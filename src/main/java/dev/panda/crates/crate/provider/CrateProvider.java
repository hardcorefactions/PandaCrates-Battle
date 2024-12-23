/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package dev.panda.crates.crate.provider;

import com.jonahseguin.drink.argument.CommandArg;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.utils.CC;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class CrateProvider
extends DrinkProvider<Crate> {
    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @Nullable
    public Crate provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws CommandExitMessage {
        String name = arg.get();
        Crate crate = Crate.getByNameIgnoreCase(name);
        if (crate == null) {
            throw new CommandExitMessage(CC.translate("&cCrate with that name does not exist!"));
        }
        return crate;
    }

    @Override
    public String argumentDescription() {
        return null;
    }

    @Override
    public List<String> getSuggestions(@NotNull String prefix) {
        return Crate.getCrates().keySet().stream().filter(name -> prefix.isEmpty() || name.startsWith(prefix)).collect(Collectors.toList());
    }
}


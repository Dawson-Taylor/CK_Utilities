package com.codekanic.ckutilities.common.items.baseitems;

import com.codekanic.ckutilities.common.items.CKUItems;
import net.minecraft.world.item.Item;

public class ItemBase extends Item {
    public ItemBase(Properties props) {
        super(props);
    }

    public ItemBase() {
        super(CKUItems.defaultProps());
    }
}

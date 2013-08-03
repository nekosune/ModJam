package dark.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import dark.common.DarkBotMain;
import dark.common.gen.DarkSchematic;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

public class ItemSpawnTool extends Item
{
    boolean flip = false;
    Pos pos;
    Pos pos2;

    public ItemSpawnTool(int par)
    {
        super(DarkBotMain.config.getItem("SpawnTool", par).getInt());
        this.setUnlocalizedName("SpawnTool");
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if (stack.getItemDamage() == 0)
            {
                if (pos == null)
                {
                    pos = new Pos(x, y, z);
                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Pos one set to " + pos.toString()));
                }
                else if (pos2 == null)
                {
                    pos2 = new Pos(x, y, z);
                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Pos2 one set to " + pos2.toString()));
                }
                if (pos2 != null && pos != null)
                {
                    new DarkSchematic("TestSeve").loadWorldSelection(world, pos, pos2).save();
                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Saved Schematic"));
                }
            }
            else if (stack.getItemDamage() == 1)
            {
                DarkSchematic scem = new DarkSchematic("TowerOne").load();
                scem.build(new PosWorld(world, player.posX, player.posY, player.posZ), true);

            }
            return true;
            // URL location = ItemSpawnTool.class.getProtectionDomain().getCodeSource().getLocation();
            //String string = location.getPath();

            //System.out.println(location.getFile());
            //McEditSchematic scem = new McEditSchematic("TowerOne").load();
            //par3EntityPlayer.setPosition(par3EntityPlayer.posX, par3EntityPlayer.posY + scem.height, par3EntityPlayer.posZ);
            //scem.build(new PosWorld(par2World, par3EntityPlayer.posX, par3EntityPlayer.posY - scem.height, par3EntityPlayer.posZ), true);
        }
        return false;
    }
}

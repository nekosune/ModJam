package dark.common.gen;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import dark.common.DarkBotMain;
import dark.common.prefab.Pair;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

/** Schematic system that is only used for creating world gen structures for this mod
 *
 * @author DarkGuardsman */
public class DarkSchematic
{
    public HashMap<Pos, Pair<Integer, Integer>> blocks = new HashMap<Pos, Pair<Integer, Integer>>();
    public static final String BlockList = "BlockList";
    /* Schematic doesn't save no vanilla blocks the same way */
    public static final String spireBlock = "B";
    public static final String spireCore = "C";
    String fileName;

    public DarkSchematic(String fileName)
    {
        this.fileName = fileName;
    }

    public DarkSchematic loadWorldSelection(World world, Pos pos, Pos pos2)
    {
        int deltaX, deltaY, deltaZ;
        Pos start = new Pos(pos.xx > pos2.xx ? pos2.xx : pos.xx, pos.yy > pos2.yy ? pos2.yy : pos.yy, pos.zz > pos2.zz ? pos2.zz : pos.zz);
        if (pos.xx < pos2.xx)
        {
            deltaX = pos2.x() - pos.x() + 1;
        }
        else
        {
            deltaX = pos.x() - pos2.x() + 1;
        }
        if (pos.yy < pos2.yy)
        {
            deltaY = pos2.y() - pos.y() + 1;
        }
        else
        {
            deltaY = pos.y() - pos2.y() + 1;
        }
        if (pos.zz < pos2.zz)
        {
            deltaZ = pos2.z() - pos.z() + 1;
        }
        else
        {
            deltaZ = pos.z() - pos2.z() + 1;
        }
        for (int x = 0; x < deltaX; ++x)
        {
            for (int y = 0; y < deltaY; ++y)
            {
                for (int z = 0; z < deltaZ; ++z)
                {
                    int blockID = world.getBlockId(start.x() + x, start.y() + y, start.z() + z);
                    int blockMeta = world.getBlockMetadata(start.x() + x, start.y() + y, start.z() + z);
                    blocks.put(new Pos(x, y, z), new Pair<Integer, Integer>(blockID, blockMeta));
                }
            }
        }
        return this;
    }

    public DarkSchematic load()
    {
        try
        {
            File file = new File(McEditSchematic.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
            NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(new FileInputStream(new File(file, fileName + ".dat")));

            NBTTagCompound blockSet = nbtdata.getCompoundTag(BlockList);
            for (int i = 0; i < blockSet.getInteger("count"); i++)
            {
                String output = blockSet.getString("Block" + i);
                String[] out = output.split(":");
                int b = 0;
                int m = 0;
                Pos pos = new Pos();
                if (out != null)
                {
                    try
                    {
                        if (out.length > 0)
                        {
                            if (out.equals(spireBlock))
                            {
                                b = DarkBotMain.blockDeco.blockID;
                            }
                            else if (out.equals(spireCore))
                            {
                                b = DarkBotMain.blockCore.blockID;
                            }
                            else
                            {
                                b = Integer.parseInt(out[0]);
                            }
                        }
                        if (out.length > 1)
                        {
                            m = Integer.parseInt(out[1]);
                        }
                        if (out.length > 2)
                        {
                            pos.xx = Integer.parseInt(out[2]);
                        }
                        if (out.length > 3)
                        {
                            pos.yy = Integer.parseInt(out[3]);
                        }
                        if (out.length > 4)
                        {
                            pos.zz = Integer.parseInt(out[4]);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    this.blocks.put(pos, new Pair<Integer, Integer>(b, m));
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public void save()
    {
        try
        {
            int sudoID = Block.sponge.blockID;

            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound blockNBT = nbt.getCompoundTag(BlockList);

            int i = 0;

            for (Entry<Pos, Pair<Integer, Integer>> entry : blocks.entrySet())
            {
                String output = "";
                output += "" + (entry.getValue().getOne() != sudoID ? entry.getValue().getOne() : 0);
                output += ":" + entry.getValue().getTwo();
                output += ":" + entry.getKey().x() + ":" + entry.getKey().y() + ":" + entry.getKey().z();
                blockNBT.setString("Block" + i, output);
                i++;
            }
            blockNBT.setInteger("count", i);
            nbt.setCompoundTag(BlockList, blockNBT);

            NBTFileSaver.saveNBTFile(fileName + ".dat", NBTFileSaver.getFolder("schematics", true), nbt, false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void build(PosWorld posWorld, boolean ignoreAir, List<Pos> ignore)
    {
        if (ignore == null)
        {
            ignore = new ArrayList<Pos>();
        }
        for (Entry<Pos, Pair<Integer, Integer>> entry : blocks.entrySet())
        {
            Pos setPos = new Pos(posWorld.xx + entry.getKey().xx, posWorld.yy + entry.getKey().yy, posWorld.zz + entry.getKey().zz);
            if (entry.getValue().getOne() != 0 && ignoreAir || !ignoreAir)
            {
                if (setPos.getTileEntity(posWorld.world) == null && !ignore.contains(setPos))
                {
                    setPos.setBlock(posWorld.world, entry.getValue().getOne(), entry.getValue().getTwo());
                }
            }
        }

    }
}

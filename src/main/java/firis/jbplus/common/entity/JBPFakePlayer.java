package firis.jbplus.common.entity;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class JBPFakePlayer extends FakePlayer {

	// UUID.randomUUID()で生成したランダムUUID
	private static final GameProfile DUMMY_PROFILE = new GameProfile(
			UUID.fromString("bbb1bdc9-380d-43d9-b0f9-aaf837d020ad"), "[JBPFakePlayer]");

	public JBPFakePlayer(World world) {
		super(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(world.provider.getDimension()),
				DUMMY_PROFILE);

	}

}
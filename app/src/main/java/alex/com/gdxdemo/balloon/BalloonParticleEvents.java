package alex.com.gdxdemo.balloon;

/**
 * @author AleXQ
 * @Date 16/2/27
 * @Description: BalloonParticle 相关事件通知
 */

public class BalloonParticleEvents {

	public static class BalloonParticleLifeCircleBegin{
		public boolean mIsSelf;

		public BalloonParticleLifeCircleBegin(boolean isSelf) {
			this.mIsSelf = isSelf;
		}

		public boolean isSelf() {
			return mIsSelf;
		}
	}

	public static class BalloonParticleLifeCircleEnd{
		public boolean mIsSelf;

		public BalloonParticleLifeCircleEnd(boolean isSelf) {
			this.mIsSelf = isSelf;
		}

		public boolean isSelf() {
			return mIsSelf;
		}
	}

}

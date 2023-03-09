import basemod.*
import basemod.interfaces.PostInitializeSubscriber
import basemod.interfaces.PreStartGameSubscriber
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.*
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue
import config.Config
import helpers.RelicPools
import java.util.*

class RelicPoolRandomizerInit : PreStartGameSubscriber, PostInitializeSubscriber {
    private lateinit var rng: Random

    companion object {
        private val settingsMenu = ModPanel()
    }

    override fun receivePostInitialize() {
        with(settingsMenu) {
            val button = object : ModLabeledToggleButton(
                "Randomize Special Event Relics",
                375f,
                675f,
                Color.WHITE.cpy(),
                FontHelper.buttonLabelFont,
                RelicPoolRandomizer.config.getBooleanKey(Config.shouldRandomizeSpecialRelicsKey),
                settingsMenu,
                {},
                {
                    RelicPoolRandomizer.config.setBooleanKey(Config.shouldRandomizeSpecialRelicsKey, it.enabled)
                }
            ){
                override fun update() {
                    super.update()

                    // Unfortunately, the hb is private, so we have to use reflection here
                    val hb = ReflectionHacks.getPrivate<Hitbox>(toggle, ModToggleButton::class.java, "hb")
                    if (hb != null && hb.hovered) {
                        TipHelper.renderGenericTip(
                            375f * Settings.xScale,
                            635f * Settings.yScale,
                            "Info",
                            "If checked, this will randomize event relics such as Golden Idol, Mark of the Bloom, etc."
                        )
                    }
                }
            }
            .also {
                this.addUIElement(it)
            }
        }
        BaseMod.registerModBadge(
            Texture("images/badge.png"),
            "Randomize Relic Pools",
            "beeyee",
            "Embrace chaos",
            settingsMenu)
    }

    override fun receivePreStartGame() {
        val shouldRandomizeSpecialRelicsKey = RelicPoolRandomizer.config.getBooleanKey(Config.shouldRandomizeSpecialRelicsKey)
        val brokenRelics: List<AbstractRelic> = listOf(Orrery(), Cauldron(), TinyHouse(), CallingBell())
        val brokenRelicIds = brokenRelics.map { it.relicId }

        // No one wants to naturally find a bad relic that literally is only negative
        val badRelics: ArrayList<AbstractRelic> = ArrayList(listOf(MarkOfTheBloom(), GremlinMask(), NlothsMask(), SpiritPoop()))
        val badRelicIds = badRelics.map { it.relicId }

        val seed = if (Settings.seed == null) SaveAndContinue.loadSaveFile(AbstractDungeon.player.chosenClass).seed else Settings.seed
        rng = Random(seed)
        val allRelics: ArrayList<AbstractRelic> = ArrayList()
        allRelics.addAll(RelicLibrary.commonList.map { it.makeCopy() })
        allRelics.addAll(RelicLibrary.uncommonList.map { it.makeCopy() })
        allRelics.addAll(RelicLibrary.rareList.map { it.makeCopy() })
        allRelics.addAll(RelicLibrary.bossList.filter { !brokenRelicIds.contains(it.relicId)  }.map { it.makeCopy() })
        allRelics.addAll(RelicLibrary.shopList.filter { !brokenRelicIds.contains(it.relicId)  }.map { it.makeCopy() })
        if (shouldRandomizeSpecialRelicsKey){
            allRelics.addAll(RelicLibrary.specialList.filter { !badRelicIds.contains(it.relicId)  }.map { it.makeCopy() })
        }

        allRelics.shuffle(rng)

        setRelicPool(allRelics, RelicLibrary.commonList.size, RelicPools.commonList)
        setRelicPool(allRelics, RelicLibrary.uncommonList.size, RelicPools.uncommonList)
        setRelicPool(allRelics, RelicLibrary.rareList.size, RelicPools.rareList)

        if (shouldRandomizeSpecialRelicsKey){
            val sublistBadRelics = rng.nextInt(badRelics.size)
            setRelicPool(allRelics, RelicLibrary.specialList.size - sublistBadRelics, RelicPools.specialList)

            val someBadRelics = badRelics.subList(0, sublistBadRelics)
            RelicPools.specialList.addAll(someBadRelics)
            RelicPools.specialList.shuffle(rng)

            someBadRelics.clear()
        }

        // Any reward screen relics MUST be Boss or Shop relics
        allRelics.addAll(brokenRelics)
        allRelics.shuffle(rng)
        setRelicPool(allRelics, RelicLibrary.bossList.size, RelicPools.bossList)

        if (shouldRandomizeSpecialRelicsKey) {
            allRelics.addAll(badRelics)
            allRelics.shuffle(rng)
        }
        setRelicPool(allRelics, RelicLibrary.shopList.size, RelicPools.shopList)

        filterOutColors(RelicPools.commonList)
        filterOutColors(RelicPools.uncommonList)
        filterOutColors(RelicPools.rareList)
        filterOutColors(RelicPools.specialList)
        filterOutColors(RelicPools.bossList)
        filterOutColors(RelicPools.shopList)
    }

    private fun setRelicPool(allRelics: ArrayList<AbstractRelic>, originalPoolSize: Int, newPool: ArrayList<AbstractRelic>) {
        val subList = allRelics.subList(0, originalPoolSize)
        newPool.clear()
        newPool.addAll(subList)
        subList.clear()
    }

    private fun filterOutColors(allRelics: ArrayList<AbstractRelic>) {
        val colorRelicsToExclude: ArrayList<AbstractRelic> = ArrayList()
        val c = CardCrawlGame.chosenCharacter
        if (c != AbstractPlayer.PlayerClass.IRONCLAD){
            colorRelicsToExclude.addAll(RelicLibrary.redList)
        }
        if (c != AbstractPlayer.PlayerClass.THE_SILENT) {
            colorRelicsToExclude.addAll(RelicLibrary.greenList)
        }
        if (c != AbstractPlayer.PlayerClass.DEFECT) {
            colorRelicsToExclude.addAll(RelicLibrary.blueList)
        }
        if (c != AbstractPlayer.PlayerClass.WATCHER) {
            colorRelicsToExclude.addAll(RelicLibrary.whiteList)
        }

        val relicIdsToExclude = colorRelicsToExclude.map { it.relicId }
        allRelics.removeIf { r -> relicIdsToExclude.contains(r.relicId) }
    }


}
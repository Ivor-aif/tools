package com.ivor.tools

object MotivationData {
    
    // 强力鼓励语句
    val powerfulMotivations = listOf(
        "你是无敌的！没有什么能阻挡你前进的步伐！",
        "现在就是最好的时机！立刻行动，成就非凡！",
        "你拥有无限的潜能！释放你内心的力量！",
        "困难只是成功路上的垫脚石！踩着它们登上巅峰！",
        "你比你想象的更强大！证明给世界看！",
        "每一秒的努力都在塑造更好的你！",
        "放弃是弱者的选择，而你是战士！",
        "你的梦想正在等待你的行动！",
        "今天的努力就是明天的奇迹！",
        "你有改变世界的能力！从现在开始！"
    )
    
    // 成功人士名言
    val successQuotes = listOf(
        "成功不是终点，失败不是末日，继续前进的勇气才最可贵。 —— 温斯顿·丘吉尔",
        "你唯一需要害怕的就是害怕本身。 —— 富兰克林·罗斯福",
        "不要等待机会，而要创造机会。 —— 乔治·萧伯纳",
        "成功就是从失败到失败，也依然不改热情。 —— 温斯顿·丘吉尔",
        "你的时间有限，不要浪费在重复别人的生活上。 —— 史蒂夫·乔布斯",
        "做你害怕做的事情，然后你会发现，不过如此。 —— 拉尔夫·沃尔多·爱默生",
        "成功的秘诀就是每天都比昨天更努力一点。 —— 雷·克洛克",
        "不要让昨天的失败影响今天的成功。 —— 约翰·麦克斯韦尔",
        "相信你能做到，你就已经成功了一半。 —— 西奥多·罗斯福",
        "最大的风险就是不冒任何风险。 —— 马克·扎克伯格"
    )
    
    // 行动指令
    val actionCommands = listOf(
        "立刻开始！不要再犹豫！",
        "现在就动手！每一秒都很宝贵！",
        "停止拖延！你的未来在等待！",
        "行动起来！让梦想变成现实！",
        "马上执行！成功属于行动者！",
        "不要再等了！机会就在眼前！",
        "立即行动！你比你想象的更有能力！",
        "现在开始！每一步都是进步！",
        "动起来！你的潜能无限！",
        "执行计划！成功就在前方！"
    )
    
    // 克服拖延的策略
    val antiProcrastinationTips = listOf(
        "将大任务分解成小步骤，一步一步来！",
        "设定明确的截止时间，给自己压力！",
        "找一个安静的环境，专注工作！",
        "关闭所有干扰，全身心投入！",
        "想象完成任务后的成就感！",
        "告诉别人你的计划，增加责任感！",
        "奖励自己每一个小进步！",
        "记住你的目标和梦想！",
        "从最简单的任务开始！",
        "保持积极的心态，相信自己！"
    )
    
    // 能量提升语句
    val energyBoosters = listOf(
        "你的能量正在爆发！感受这股力量！",
        "你是能量的源泉！释放你的光芒！",
        "充满活力！你可以征服一切！",
        "你的激情点燃了整个世界！",
        "能量满满！没有什么能阻挡你！",
        "你是行走的能量炸弹！",
        "感受你内心的火焰在燃烧！",
        "你的能量可以移山倒海！",
        "充电完毕！准备创造奇迹！",
        "你是不可阻挡的力量！"
    )
    
    // 获取随机鼓励内容
    fun getRandomMotivation(): String {
        return powerfulMotivations.random()
    }
    
    fun getRandomQuote(): String {
        return successQuotes.random()
    }
    
    fun getRandomActionCommand(): String {
        return actionCommands.random()
    }
    
    fun getRandomTip(): String {
        return antiProcrastinationTips.random()
    }
    
    fun getRandomEnergyBooster(): String {
        return energyBoosters.random()
    }
    
    // 获取指定类型的随机内容
    fun getRandomContentByType(type: String): String {
        return when (type) {
            "motivation" -> getRandomMotivation()
            "quote" -> getRandomQuote()
            "action" -> getRandomActionCommand()
            "tip" -> getRandomTip()
            "energy" -> getRandomEnergyBooster()
            else -> getRandomMotivation()
        }
    }
    
    // 获取类型显示名称
    fun getTypeDisplayName(type: String): String {
        return when (type) {
            "motivation" -> "强力鼓励"
            "quote" -> "成功名言"
            "action" -> "行动指令"
            "tip" -> "克服拖延"
            "energy" -> "能量提升"
            else -> "强力鼓励"
        }
    }
}
# idea插件 #

    <extensions defaultExtensionNs="com.intellij">
        <applicationConfigurable id="test" groupId="test" provider="com.test.config.MyConfigProvider"/>
        <applicationService serviceImplementation="com.test.config.Setting" serviceInterface="com.test.config.Setting"/>
    </extensions>

- `applicationConfigurable` **插件配置面板**

> Provider实现 ConfigurableProvider

> Config实现 ConfigurableBase<{SettingUI}, {Setting}>

- `applicationService` **持久化存储**

> 实现PersistentStateComponent<Setting.State>
>
> 注解

    @State(
        name = "Setting",
        defaultStateAsResource = false,
        storages = {
                @Storage("test_setting.xml")
        }
	)

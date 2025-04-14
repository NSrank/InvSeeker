

# InvSeeker  | 让我看看！ - Minecraft 玩家背包/容器查看/记录插件  
[![License](https://img.shields.io/badge/License-GPLv3-green.svg)]()  

**InvSeeker** （中文名：让我看看！）是一款基于 **Paper 1.20.1** 的 Minecraft 插件，允许管理员通过可视化 GUI 查看在线/离线玩家的背包及末影箱内容。
还可以记录玩家容器的操作交互！

> **注意**：本插件由 AI 开发，旨在帮助服务器管理员更高效地管理玩家封禁行为。

---

## 功能特性
- ✅ **可视化 GUI 界面**：直观展示玩家物品栏。
- ✅ **容器操作日志**：可以将玩家容器的操作记录下来。
- ✅ **支持离线玩家**：即使玩家不在线也能查看其背包。
- ✅ **权限控制**：通过 `invseeker.view` 和 `invseeker.admin` 管理访问权限。
- ✅ **防止篡改**：禁止在 GUI 中修改玩家物品。

---

## 注意事项
请事先安装**前置插件**：[Item-NBT-API](https://github.com/tr7zw/Item-NBT-API/)   
前置插件缺失会导致该插件无法正常运行

---

## 安装步骤
1. **下载插件**  
   - 从 [Github Release](https://github.com/NSrank/InvSeeker/releases/) 获取最新版本的 JAR 文件。
   - 下载**前置插件** [Item-NBT-API](https://github.com/tr7zw/Item-NBT-API/) 并将其放入 `plugins/` 目录。


2. **部署到服务器**  
   ```powershell
   # 将插件放入服务器 plugins 目录
   cp InvSeeker.jar plugins/
   ```

3. **重启服务器**
   ```powershell
   ./start.sh  # 或使用你的服务器启动脚本
   ```

---

## 使用说明
### 命令
| 命令                | 权限节点              | 功能          |
|-------------------|-------------------|-------------|
| `/invsee <玩家名>`   | `invseeker.view`  | 打开玩家背包 GUI  |
| `/endersee <玩家名>` | `invseeker.view`  | 打开玩家末影箱 GUI |
| `/invsee help`    | `invseeker.view`  | 显示帮助信息      |
| `/invsee reload`  | `invseeker.admin` | 重载配置文件      |


---

## 配置文件
在 `plugins/InvSee/config.yml` 中可自定义以下选项：
```yaml

# 语言设置（支持 en_US/zh_CN）
language: zh_CN

# 日志记录
enable-container-logging: true
```

---
## 开源协议
本项目遵循 **GNU General Public License v3.0**
- 你可以自由使用、修改和分发此插件。
- 修改后的代码需保持开源并注明原始来源。
- 完整协议详见 [LICENSE](https://www.gnu.org/licenses/gpl-3.0.txt) 文件。

---

## 贡献指南
1. **Fork 本仓库**
2. 创建你的功能分支 (`git checkout -b feature/YourFeature`)
3. 提交代码 (`git commit -m 'Add new feature'`)
4. 推送到分支 (`git push origin feature/YourFeature`)
5. 提交 Pull Request

---

## 支持与反馈
- 遇到问题？请在 [GitHub Issues](https://github.com/NSrank/InvSeeker/issues) 提交详细描述。
---

### 版权声明
- 开发声明 ：本插件由 AI 开发，旨在为 Minecraft Velocity 社区提供高效的封禁管理工具。
- 许可证 ：本插件遵循 GNU General Public License v3.0 许可证，您可以自由使用、修改和分发，但需遵守许可证条款。
- 免责条款 ：开发者不对因使用本插件而导致的任何问题负责。

---

### 特别感谢
感谢以下技术和工具对本插件的支持：

- [Item-NBT-API](https://github.com/tr7zw/Item-NBT-API/)

---

**插件开源地址**：[GitHub 仓库](https://github.com/NSrank/InvSeeker)
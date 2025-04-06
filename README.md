

# InvSeeker - Minecraft 玩家背包查看插件  
[![License](https://img.shields.io/badge/License-GPLv3-green.svg)]()  

**InvSeeker** 是一款基于 **Paper 1.20.1** 的 Minecraft 插件，允许管理员通过可视化 GUI 查看在线/离线玩家的背包及末影箱内容。
**注：这是一款100%基于AI开发的插件，由AI生成，仅作为学习交流使用，请勿用于商业用途。**
---

## 功能特性
- ✅ **可视化 GUI 界面**：直观展示玩家物品栏。
- ✅ **支持离线玩家**：即使玩家不在线也能查看其背包。
- ✅ **权限控制**：通过 `invsee.view` 和 `invsee.admin` 管理访问权限。
- ✅ **防止篡改**：禁止在 GUI 中修改玩家物品。

---

## 安装步骤
1. **下载插件**  
   - 从 [Github Release](https://github.com/NSrank/InvSeeker/releases/) 获取最新版本的 JAR 文件。

2. **部署到服务器**  
   ```powershell
   # 将插件放入服务器 plugins 目录
   cp InvSee-1.0.jar plugins/
   ```

3. **重启服务器**
   ```powershell
   ./start.sh  # 或使用你的服务器启动脚本
   ```

---

## 使用说明
### 命令
| 命令                  | 权限节点          | 功能                     |
|-----------------------|-------------------|--------------------------|
| `/invsee <玩家名>`    | `invsee.view`     | 打开玩家背包 GUI         |
| `/endersee <玩家名>`  | `invsee.view`     | 打开玩家末影箱 GUI       |
| `/invsee reload`      | `invsee.admin`    | 重载配置文件             |

### GUI 界面
- **返回按钮**：点击左下角箭头返回。
- **玩家头像**：右下角显示目标玩家头像。

---

## 配置文件
在 `plugins/InvSee/config.yml` 中可自定义以下选项：
```yaml
# 是否允许修改离线玩家物品（谨慎开启）
allow-edit-offline: false

# 语言设置（支持 en_US/zh_CN）
language: zh_CN

# 日志记录
log-view-actions: true
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

**© 2025 NSrank. 保留所有权利。**  
**插件开源地址**：[GitHub 仓库](https://github.com/NSrank/InvSeeker)
```
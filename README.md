# MechatechKt

TNCT-Mechatech Discordグループで運用するDiscord Botです。

## 機能
- メンタリング用プライベートチャンネルの自動作成
"mentor"コマンドを使って、メンタリング用のプライベートチャンネルを作成します。  
[PR #1](https://github.com/TNCT-Mechatech/MechatechKt/pull/1)

<img width="397" alt="Screenshot 2023-07-30 at 0 01 44" src="https://github.com/TNCT-Mechatech/MechatechKt/assets/49048811/eb6895b8-5bcb-4251-a015-6fb6dabee046">
<img width="518" alt="Screenshot 2023-07-30 at 0 02 09" src="https://github.com/TNCT-Mechatech/MechatechKt/assets/49048811/63b14a45-b4a4-4b30-8ad4-6a190a527efd">

## 導入方法

### docker-compose
configディレクトリに設定ファイルが生成されるので、設定後再起動してください。
```yaml
version: '3'
services:
  bot:
    image: ghcr.io/tnct-mechatech/mechatechkt:main
    restart: always
    environment:
      - TZ=Asia/Tokyo
    volumes:
    - ./config:/app/config
    working_dir: /app
```
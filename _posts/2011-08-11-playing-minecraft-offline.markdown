---
layout: post
title: Playing Minecraft Offline
categories:
- minecraft
- matrix
---

Me and my friends like to play Minecraft sometimes, but it often happens to be in the middle of nowhere, without internet. The most interesting part is most often the setup, not the playing.

We usually start by sharing the latest Minecraft binaries and setting up an ad-hoc WiFi network. Then someone boots a server, and we're set... NOT!

Minecraft usually performs some DRM and verification with the Minecraft website. For the server, this can be disabled by setting [`online-mode` to `false` in `server.properties`][1], but with the client, you're out of luck.

Initially, the solution consisted of sending someone with a smartphone out to find an open WiFi network. Later, before the new launcher, it used to be possible to fake the login server[^2]. But recently, I found a way to play in offline mode on the client as well.

**Spoiler alert**: Minecraft is The Matrix.

![Minecraft Matrix](/images/mine_matrix.png)

That's it. It had to be said. Now, let's hack Minecraft Matrix style.

Normally, when you click "Play Offline" after a failed login, you are named "Player", which means that you are going to kick each other because you all have the same name.

Now, fire up your terminals[^4], and change directory to the `bin` folder of your Minecraft directory[^5].

Now, I can't read and write Matrix like Mouse[^3], but luckily, the GNU toolchain can. What we're going to do is change a few occurrences of "Player" with any string of equal length[^6].

The first step is to extract the jar. It turns out it works just fine as a directory, and it's much easier to work with that way.

    mv minecraft.jar minecraft-orig.jar
    mkdir minecraft.jar
    cd minecraft.jar
    jar -xf ../minecraft-orig.jar

Now we need to figure out which files need to be modified. Beware of Déjà vu!

    grep -r Player .
    # Binary file ./ei.class matches
    # ./lang/stats_US.lang:stat.playerKills=Player Kills
    # Binary file ./net/minecraft/client/Minecraft.class matches
    # Binary file ./net/minecraft/client/MinecraftApplet.class matches
    # Binary file ./ow.class matches

Some experimentation shows that `MinecraftApplet.class` is the one that matters. Now you need to use `sed` to replace "Player" with another name of equal length[^6].

Mac users will need to install GNU sed, as BSD sed scrambles the binary file beyond repair. I used `brew install gnu-sed`, but Macports and Fink might also work.

    gsed -ibak s/Player/_Notch/g net/minecraft/client/MinecraftApplet.class

Done!

[1]: http://www.minecraftwiki.net/wiki/Server.properties#online-mode
[^2]: Not anymore, it uses SSL now.
[^3]: Mouse, please tell me how you wrote the lady in the red dress.
[^4]: Mac users can use the "Homebrew" style at fullscreen for extra Matrix factor.
[^5]: Mac: `~/Library/Application\ Support/minecraft/bin`, Linux: `~/.minecraft/bin`, Windows: `~/.AppData/Roaming/.minecraft/bin`
[^6]: Equal length you hear me, you'll crash Minecraft otherwise.

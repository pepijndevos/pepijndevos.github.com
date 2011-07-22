---
layout: post
title: Lion Hacks
categories:
- mac
- lion
- time machine
---

I installed Mac OS X 10.7 Lion on my Mac yestereday. It did not went very smooth. I'm not going to do [a review][1], but I *am* going to share my story and some tricks used along the way.

### Backup

**Always backup**, always, and make sure it works.

I run Time Machine backups on an external 500GB HDD, but TM has the nasty habbit to gobble up all available disk space. So when I wanted to make a disk image of my complete HD, I had to remove the TM backup.

Then I did something stupid, and proceeded with the installation without checking the image worked.

#### Trick: Remove Single Revision

Go into Time Machine to the revision and folder you want to remove. Now right-click and select "Delete Backup".

Note that the UI is not suitable for removing all but a few revisions, which is why I opted to remove the whole thing.

#### Trick: Limit Time Machine space

Only after I was finally running Lion did I discovered a way to limit the disk space used by Time Machine.

There are [a few][3] [complicated hints][4] to achieve this for remote volumes, but nothing for local ones.

This trick involves another Mac, because Lion refuses to connect to a "remote" disk that is actually the local disk shared over AFP.

First, we need to trick Time Machine into using a sparsebundle instead of plain files.

 1. Connect the disk to another Mac
 2. Share it in System Preferences
 3. Initiate a backup, so that a sparsebundle is created
 4. Cancel the backup and disconnect the drive

Now Time Machine will continue using the disk image when the drive is connected directly. The only thing that remains is to limit the bundle in size, using this command:

    hdiutil resize -size 150G -shrinkonly /path/to/image.sparsebundle

Where `150G` and `/path/to/image.sparsebundle` need to be replaced with the desired size and the path to the bundle, residing on your backup drive.

### Installation

You're normally supposed to install Lion straight from the App Store,
but [some smart guy found out][2] you can burn your own DVD or USB drive.

I ended up going the DVD route, because my main drive appeared unbootable by the installer and my USB stick was a *few MB* to small.

#### *Possible* trick: Clean install without DVD

I could not try this because of my broken HD, please let me know if it works.

Lion installs a recovery partition, which plays an important role in the installation. You can't simply replace a running system, you know.

Run the installation straight from the App Store up to the point where you need to reboot. Now the trick is to hold &#8984;R to boot into the recovery mode rather than into the installer.

Now, use Disk Utility to wipe your disk, and run the full installation from the recovery partition.

### Migration/Recovery

As I said, I did a clean install, wiped my drive, but forgot to check the image works. *tension...* It did not work of course.

The right way, of course is to always make sure your Time Machine backup or your disk image works, and to verify and repair your hard drive before you make an image or upgrade.

If you do all of that, Mac has a neat Migration Assistent which can import your old data.

#### Trick: Mount correct image of damanged partition

Disk Utility is able to perform first aid on damaged disk images, but apparently not mine. Another weird thing is that the checksum matched, but no mountable volumes where found.

So what I have is a **working** image of a **broken** disk.

Luckly HFS+ has a feature called journaling, to recover from a bad state, but Disk Utility told me that the journal could not be replayed because the media was read-only.

After using Disk Utility to convert the disk image to read/write, it would mount, and let me extract my files, but upon applying first aid, matters got worse again, and Disk Utility told me to take my files and ruuuun!

### Settling in

This part went pretty smooth. I'm swapping some software, waiting for some  Lion updates, and looking at all the things that go *swoosh*.

Homebrew is in, Macports is out. Homebrew makes sysadmins cringe, because it's written by a "Ruby hippie" that has no sens for dependency management. It does not include its own copy of everything included in OS X, which saves a lot of time and space IMO.

I read iChat is now extensible to support "legacy" protcols, like MSN. Because I'm barely using MSN and becaus everyone is hiring Adium devs, I'm using iChat now. I'll wait until someone staples libpurple to iChat.

I also heard Growl 1.3 will be a sexy Lion app available in the App Store. Not sure if I'll wait or install 1.2 anyway.

One thing that *has* to be done in Lion is un-dumbing Finder.

 1. Customize the toolbar to include that path widget
 2. [Show full paths][5] `defaults write com.apple.finder _FXShowPosixPathInTitle -bool YES`
 3. [Show hidden files][6] `defaults write com.apple.finder AppleShowAllFiles -bool YES`

Then restart finder, ` killall Finder`

Good luck!

[1]: http://arstechnica.com/apple/reviews/2011/07/mac-os-x-10-7.ars
[2]: http://www.eggfreckles.net/tech/installing-lion-clean/
[3]: http://hints.macworld.com/article.php?story=20080519051720677
[4]: http://hints.macworld.com/article.php?story=20071108020121567
[5]: http://www.tuaw.com/2008/12/05/terminal-tips-enable-path-view-in-finder/
[6]: http://lifehacker.com/188892/show-hidden-files-in-finder

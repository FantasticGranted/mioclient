# Mio Client

A client-side utility mod for **Minecraft 26.1.2** (Fabric).

- Mod id: `mioclient`
- Minecraft: 26.1.2 (unobfuscated — no mappings required)
- Fabric Loader: >= 0.19.5
- Environment: client only

## Install

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 26.1.2.
2. Drop `mioclient-2.1.7-26.1.2.jar` into your `.minecraft/mods` folder.
3. Fabric API is required.

## Build

```sh
./gradlew build
```

The jar is produced in `build/libs`.

## CI

GitHub Actions builds every push (`.github/workflows/build.yml`) and uploads the jar as an artifact.

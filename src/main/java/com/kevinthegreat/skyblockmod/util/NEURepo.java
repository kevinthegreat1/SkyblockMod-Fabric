package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import org.eclipse.jgit.api.Git;

import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NEURepo {
    private static final String REPO_URL = "https://github.com/NotEnoughUpdates/NotEnoughUpdates-REPO.git";
    private final CompletableFuture<Void> repoInitialized;

    public NEURepo() {
        repoInitialized = CompletableFuture.runAsync(() -> {
            try {
                if (Files.isDirectory(SkyblockMod.NEU_REPO_DIR)) {
                    try (Git localRepo = Git.open(SkyblockMod.NEU_REPO_DIR.toFile())) {
                        localRepo.pull().call();
                    }
                } else {
                    Git.cloneRepository().setURI(REPO_URL).setDirectory(SkyblockMod.NEU_REPO_DIR.toFile()).setBranchesToClone(List.of("refs/heads/master")).setBranch("refs/heads/master").call().close();
                }
                SkyblockMod.LOGGER.info("[Skyblock Mod] NEU Repo Initialized.");
            } catch (Exception e) {
                SkyblockMod.LOGGER.error("[Skyblock Mod] Failed to initialize NEU Repo", e);
            }
        });
    }

    public CompletableFuture<Void> runAsyncAfterLoad(Runnable runnable) {
        return repoInitialized.thenRunAsync(runnable);
    }
}

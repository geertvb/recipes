# Creating git repository in the data directory

Create a git directory in the data directory and go inside.
For example `playground.git`

```shell
mkdir playground.git
cd playground.git
```

Initialize as a bare git repository

```shell
git init --bare .
```

Update the server info

```shell
git update-server-info
```

And create the post-update hook

```shell
mv hooks/post-update.sample hooks/post-update
```

# Cloning the repository

## Dumb

```shell
git clone http://localhost:8080/playground.git
```

## Smart

```shell
git clone http://localhost:8080/git/playground.git
```
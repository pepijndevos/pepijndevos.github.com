---
layout: post
title: Claude by the token in Open WebUI
categories:
- machinelearning
---

Last month I subscribed to Claude Pro, but was dismayed to learn it doesn't give you API access to use it in VS Code or Home Assistant or whatever.
So I didn't renew my subscription and instead bought API access, thinking I'd just use some chat app.
Turns out it's not that easy to find a good chat app where you can just plug in your API token.

The solution I settled on is to use [LiteLLM](https://docs.litellm.ai/) with [Open WebUI](https://docs.openwebui.com/).
Open WebUI is a great chat interface that is primarily used with [Ollama](https://ollama.com/), but it also supports OpenAI compatible APIs.
LiteLLM is a proxy that translates a ton of LLMs to a unified OpenAPI compatible API.
Badabing badaboom, give LiteLLM your Anthropic key, plug it into Open WebUI and bob's your uncle.

It's actually great if you are a very heavy or very casual user because you pay by the token.
That means if you use it only a little, it's cheaper than Claude Pro, and if you use it a lot, you aren't limited to a certain amount of messages.
Surprisingly it also does better RAG than Claude, letting you do web searches and include more and bigger documents than would fit in the context window.

Here is my Docker compose file to set it all up.
It is modified from [ollama-intel-gpu](https://github.com/mattcurf/ollama-intel-gpu) to include LiteLLM.
But if you're on team green or red, you can just change the first image to use [`ollama/ollama`](https://hub.docker.com/r/ollama/ollama) I suppose.

```
services:
  ollama-intel-gpu:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: ollama-intel-gpu
    image: ollama-intel-gpu:latest
    restart: always
    devices:
      - /dev/dri:/dev/dri
    volumes:
      - ollama-intel-gpu:/root/.ollama
    ports:
      - "11434:11434"
  ollama-webui:
    image: ghcr.io/open-webui/open-webui:latest
    container_name: ollama-webui
    volumes:
      - ollama-webui:/app/backend/data
    depends_on:
      - ollama-intel-gpu
      - litellm
    ports:
      - ${OLLAMA_WEBUI_PORT-3000}:8080
    environment:
      - OLLAMA_BASE_URL=http://ollama-intel-gpu:11434
      - OPENAI_API_BASE_URL=http://litellm:4000
    extra_hosts:
      - host.docker.internal:host-gateway
    restart: always
  litellm:
    image: ghcr.io/berriai/litellm:main-latest
    container_name: litellm
    volumes:
      - ./litellm_config.yaml:/app/config.yaml
    ports:
      - 4000:4000
    environment:
      - ANTHROPIC_API_KEY=YOURKEYHERE
    restart: always
    command: --config /app/config.yaml
volumes:
  ollama-webui: {}
  ollama-intel-gpu: {}
```
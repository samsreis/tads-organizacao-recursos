const estado = { token: null, perfil: null };

function el(id) { return document.getElementById(id); }
function mostrar(id, texto) {
  const e = el(id);
  e.textContent = texto || "";
  e.classList.toggle("ativo", !!texto);
}
function formatarData(v) { return v ? new Date(v).toLocaleString("pt-BR") : ""; }
function isoLocal(v) { return v ? new Date(v).toISOString() : null; }

async function api(caminho, opcoes = {}) {
  const headers = Object.assign({ "Content-Type": "application/json" }, opcoes.headers || {});
  if (estado.token) headers["Authorization"] = "Bearer " + estado.token;
  const r = await fetch(caminho, Object.assign({}, opcoes, { headers }));
  let corpo = null;
  const txt = await r.text();
  if (txt) { try { corpo = JSON.parse(txt); } catch { corpo = { erro: txt }; } }
  if (!r.ok) {
    const msg = (corpo && corpo.erro) ? corpo.erro : ("Erro " + r.status);
    throw new Error(msg);
  }
  return corpo;
}

function trocarTela(nome) {
  ["login","salas","minhas","aprovar"].forEach(t => {
    el("tela-" + t).hidden = (t !== nome);
  });
  el("nav").hidden = !estado.token;
  if (nome === "salas") carregarSalas();
  if (nome === "minhas") carregarMinhas();
  if (nome === "aprovar") carregarAprovar();
}

el("formLogin").addEventListener("submit", async (e) => {
  e.preventDefault();
  mostrar("msgLogin", "");
  try {
    const r = await api("/auth/login", {
      method: "POST",
      body: JSON.stringify({ email: el("loginEmail").value, senha: el("loginSenha").value })
    });
    estado.token = r.token;
    estado.perfil = r.perfil;
    el("btnAprovar").hidden = !(r.perfil === "RESPONSAVEL" || r.perfil === "ADMINISTRADOR");
    trocarTela("salas");
  } catch (err) { mostrar("msgLogin", err.message); }
});

el("btnSair").addEventListener("click", () => {
  estado.token = null; estado.perfil = null;
  el("btnAprovar").hidden = true;
  trocarTela("login");
});

document.querySelectorAll("nav button[data-tela]").forEach(b => {
  b.addEventListener("click", () => trocarTela(b.dataset.tela));
});

async function carregarSalas() {
  mostrar("msgSalas", "");
  const params = new URLSearchParams();
  const cap = el("filtroCapacidade").value;
  const loc = el("filtroLocal").value;
  const ini = el("filtroInicio").value;
  const fim = el("filtroFim").value;
  if (cap) params.append("capacidadeMinima", cap);
  if (loc) params.append("localizacao", loc);
  if (ini && fim) { params.append("inicio", isoLocal(ini)); params.append("fim", isoLocal(fim)); }
  try {
    const lista = await api("/salas?" + params.toString());
    const ul = el("listaSalas"); ul.innerHTML = "";
    const sel = el("reservaSala"); sel.innerHTML = "";
    lista.forEach(s => {
      const li = document.createElement("li");
      li.textContent = s.nome + " | cap " + s.capacidade + " | " + s.localizacao + (s.emManutencao ? " | manutencao" : "");
      ul.appendChild(li);
      const op = document.createElement("option");
      op.value = s.id; op.textContent = s.nome;
      sel.appendChild(op);
    });
    if (lista.length === 0) ul.innerHTML = "<li>Nenhuma sala encontrada</li>";
  } catch (err) { mostrar("msgSalas", err.message); }
}

el("formFiltro").addEventListener("submit", (e) => { e.preventDefault(); carregarSalas(); });

el("formReserva").addEventListener("submit", async (e) => {
  e.preventDefault();
  mostrar("msgReserva", "");
  try {
    await api("/reservas", {
      method: "POST",
      body: JSON.stringify({
        salaId: Number(el("reservaSala").value),
        inicio: isoLocal(el("reservaInicio").value),
        fim: isoLocal(el("reservaFim").value)
      })
    });
    mostrar("msgReserva", "Reserva solicitada");
  } catch (err) { mostrar("msgReserva", err.message); }
});

async function carregarMinhas() {
  mostrar("msgMinhas", "");
  try {
    const lista = await api("/reservas/minhas");
    const ul = el("listaMinhas"); ul.innerHTML = "";
    lista.forEach(r => {
      const li = document.createElement("li");
      li.textContent = "Reserva " + r.id + " | sala " + (r.salaId ?? "") + " | " + formatarData(r.inicio) + " - " + formatarData(r.fim) + " | " + r.status;
      ul.appendChild(li);
    });
    if (lista.length === 0) ul.innerHTML = "<li>Sem reservas</li>";
  } catch (err) { mostrar("msgMinhas", err.message); }
}

async function carregarAprovar() {
  mostrar("msgAprovar", "");
  try {
    const lista = await api("/reservas/minhas");
    const ul = el("listaAprovar"); ul.innerHTML = "";
    lista.forEach(r => {
      const li = document.createElement("li");
      li.textContent = "Reserva " + r.id + " | " + formatarData(r.inicio) + " | " + r.status + " ";
      ["aprovar","rejeitar","iniciar","concluir"].forEach(acao => {
        const b = document.createElement("button");
        b.textContent = acao;
        b.addEventListener("click", async () => {
          try { await api("/reservas/" + r.id + "/" + acao, { method: "POST" }); carregarAprovar(); }
          catch (err) { mostrar("msgAprovar", err.message); }
        });
        li.appendChild(b);
      });
      ul.appendChild(li);
    });
    if (lista.length === 0) ul.innerHTML = "<li>Sem reservas</li>";
  } catch (err) { mostrar("msgAprovar", err.message); }
}

trocarTela("login");

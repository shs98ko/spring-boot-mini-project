const articleContainer = document.getElementById("articleContainer");
const form = document.getElementById("commentForm");
const deleteBtn = document.querySelectorAll(".deleteBtn");
const alertDiv = document.querySelector(".alert-danger");

const addComment = (text, id) => {
    const articleComments = document.querySelector(".article__comments ul");
    const newComment = document.createElement("li");
    newComment.dataset.id = id;
    newComment.className = "article__comment";
    const span = document.createElement("span");
    span.innerText = ` ${text}`;
    const span2 = document.createElement("span");
    span2.innerText = "❌";
    newComment.appendChild(span);
    newComment.appendChild(span2);
    articleComments.prepend(newComment);
    span2.addEventListener("click", handleDelete);
};

const handleDelete = async (event) => {
    const commentList = event.target.parentNode;
    const commentId = commentList.dataset.id;
    const articleId = articleContainer.dataset.id;
    const response = await fetch(`/api/comments/${commentId}/delete`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ articleId }),
    });

    if (response.status === 200) {
        deleteComment(event);
    }
    if (response.status === 403) {
        alert("댓글 주인이 아닙니다.");
    }
};

const deleteComment = (event) => {
    const commentContainer = document.querySelector(".article__comments ul");
    const commentList = event.target.parentNode;
    commentContainer.removeChild(commentList);
};

const handleSubmit = async (event) => {
    event.preventDefault();
    const textarea = form.querySelector("textarea");
    const text = textarea.value;
    const articleId = articleContainer.dataset.id;
    if (text === "") {
        return;
    }
    const response = await fetch(`/api/articles/${articleId}/comment`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ text }),
    });
    if (response.status === 201) {
        textarea.value = "";
        const { newCommentId } = await response.json();
        //const data = await response.json();
        //const newCommentId = data.newCommentId;
        addComment(text, newCommentId);
    }else if(response.status === 404){
        const { errorMessage } = await response.json();
        // mustache의 alert div에 에러 표시
        alertDiv.innerText = errorMessage;
        alertDiv.style.display = "block";
    }
};

if (form) {
    form.addEventListener("submit", handleSubmit);
}

for (let i = 0; i < deleteBtn.length; i++) {
    deleteBtn[i].addEventListener("click", handleDelete);
}
